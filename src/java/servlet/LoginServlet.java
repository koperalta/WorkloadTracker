package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import exception.*;
import helper.*;

public class LoginServlet extends HttpServlet {

    private String dbUrl, dbUser, dbPass, dbDriver;

    public void init() throws ServletException {
        ServletContext sc = getServletContext();
        dbUrl = sc.getInitParameter("derbyURL");
        dbUser = sc.getInitParameter("derbyUsername");
        dbPass = sc.getInitParameter("derbyPassword");
        dbDriver = sc.getInitParameter("derbyDriver");

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            throw new ServletException("Driver not found", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String key = getServletContext().getInitParameter("secretKey");
        String instance = getServletContext().getInitParameter("cipherInstance");
        String gCaptchaResponse = request.getParameter("g-recaptcha-response");

        boolean captchaValid = CaptchaServlet.verify(gCaptchaResponse);
        if (!captchaValid) {
            request.setAttribute("errorMessage", "Please verify that you are not a robot.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (isInvalid(user) && isInvalid(pass)) {
            throw new NullValueException("Missing Fields");
        }

        if (!isInvalid(pass)) {
            pass = Encrypter.encrypt(pass, key, instance);
        }

        String query = "SELECT U.USER_ID, U.USERNAME, U.PASSWORD_HASH, UR.ROLE_ID "
                + "FROM DERBY_USERS U "
                + "LEFT JOIN DERBY_USER_ROLES UR ON U.USER_ID = UR.USER_ID "
                + "WHERE U.USERNAME = ?";

        try (
                Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, user);

            try (ResultSet rs = ps.executeQuery()) {
                boolean validUser = rs.next();

                if (!validUser && isInvalid(pass)) {
                    request.setAttribute("errorMessage", "The username is incorrect and the password field cannot be left empty!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }
                if (!validUser) {
                    request.setAttribute("errorMessage", "The username is invalid!");
                    request.getRequestDispatcher("login.jsp").forward(request, response);
                    return;
                }

                String storedPass = rs.getString("PASSWORD_HASH");
                int role = rs.getInt("ROLE_ID");

                // Inside LoginServlet.java
                if (storedPass.equals(pass)) {
                    int userId = rs.getInt("USER_ID");
                    HttpSession session = request.getSession();
                    session.setAttribute("loggedUsername", user);
                    session.setAttribute("loggedUserId", userId);
                    session.setAttribute("loggedUserRoleId", role);
    
                    if(role == 2) //student
                        response.sendRedirect("FetchWorkloadServlet");
                    else //admin
                        response.sendRedirect("AdminDashboardServlet");
                    return;
                } else {
                    throw new AuthenticationException("Incorrect Username and Password");
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        } catch (AuthenticationException e) {
            request.setAttribute("errorMessage", e);
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }
    }

    private boolean isInvalid(String input) {
        return input == null || input.trim().isEmpty();
    }
}

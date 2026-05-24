package servlet;



import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Get the current session, if it exists
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // 2. Invalidate the session to clear all attributes
            session.invalidate();
        }
        
        // 3. Redirect back to the login page
        response.sendRedirect("login.jsp");
    }
}
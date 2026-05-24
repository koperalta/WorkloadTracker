package workloadtracker;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FetchWorkloadServlet extends HttpServlet {

    private WorkloadDAO workloadDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Pass the ServletContext so the DAO can read web.xml parameters
            workloadDAO = new WorkloadDAO(getServletContext());
        } catch (ClassNotFoundException e) {
            // Catch the exception if the MySQL driver fails to load
            throw new ServletException("Failed to initialize WorkloadDAO: MySQL driver not found.", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        // Temporarily add this at the start of your doGet to bypass login, only uncomment this to test the code and the login logic hasn't been implemented by Lance yet. 
        // HttpSession session = request.getSession(true);
        // session.setAttribute("loggedUserId", 2); // 2 is the ID for a student in your Derby data

        // 1. Security Check: Ensure user is actively logged in via Derby authentication
        HttpSession session = request.getSession(); // Make sure to uncomment this once Lance has implemented the login logic
        if (session == null || session.getAttribute("loggedUserId") == null) {
            // Unauthorized access, redirect back to login
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            // 2. Fetch the logged-in user's ID
            int userId = (Integer) session.getAttribute("loggedUserId");
            
            // 3. Query the Workload Core (MySQL)
            List<StudentTask> userWorkload = workloadDAO.getTasksForStudent(userId);
            
            // 4. Bind data to request and forward to the frontend JSP
            request.setAttribute("workloadData", userWorkload);
            request.getRequestDispatcher("/student_dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            // Usability Constraint: Catch all exceptions, hide stack traces, redirect to custom error page
            request.setAttribute("errorTitle", "Data Retrieval Error");
            request.setAttribute("errorMessage", "Our system encountered an error while loading your workload. Please try again later.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
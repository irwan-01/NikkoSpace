package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.connection.AzureSqlDatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/SignupController")
public class SignupController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public SignupController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String gender = request.getParameter("gender");

        if (!password.equals(confirmPassword)) {
            // Passwords do not match
            request.setAttribute("errorMessage", "Passwords do not match!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
            dispatcher.forward(request, response);
            return;
        }

       // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String userId = null;
        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO users (username, password, email, phoneNumber, birthDate, gender) VALUES (?, ?, ?, ?, ?, ?)")) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setDate(5, java.sql.Date.valueOf(birthDate)); // Convert to SQL Date
            ps.setString(6, gender);
            
            int result = ps.executeUpdate();
            if (result > 0) {
                // Signup successful
                response.sendRedirect("login.jsp");
                HttpSession session = request.getSession();
                session.setAttribute("userId", userId); // Get userId using a SELECT query for the newly created user
                response.sendRedirect("ProfileController");
            } else {
                // Signup failed
                request.setAttribute("errorMessage", "An error occurred during signup. Please try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
            dispatcher.forward(request, response);
        }
    }
}

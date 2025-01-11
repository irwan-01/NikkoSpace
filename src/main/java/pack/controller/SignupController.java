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
import java.sql.ResultSet;

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

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement psInsert = con.prepareStatement("INSERT INTO users (username, password, email, phoneNumber, birthDate, gender) VALUES (?, ?, ?, ?, ?, ?)");
             PreparedStatement psSelect = con.prepareStatement("SELECT user_id FROM users WHERE email = ?")) {
            
            // Insert the new user
            psInsert.setString(1, username);
            psInsert.setString(2, hashedPassword); // Save the hashed password
            psInsert.setString(3, email);
            psInsert.setString(4, phoneNumber);
            psInsert.setDate(5, java.sql.Date.valueOf(birthDate)); // Convert to SQL Date
            psInsert.setString(6, gender);
            
            int result = psInsert.executeUpdate();
            if (result > 0) {
                // Retrieve the newly created user's ID
                psSelect.setString(1, email);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        int user_id = rs.getInt("user_id");
                        // Store user_id in session
                        HttpSession session = request.getSession();
                        session.setAttribute("user_id", user_id);
                        response.sendRedirect("ProfileController");
                        return;
                    }
                }
            }

            // If something goes wrong
            request.setAttribute("errorMessage", "An error occurred during signup. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("signup.jsp");
            dispatcher.forward(request, response);
        }
    }
}

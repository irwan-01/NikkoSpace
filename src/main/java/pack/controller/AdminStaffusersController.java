package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.connection.AzureSqlDatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException; // Import SQLException

@WebServlet("/AdminStaffusersController")
public class AdminStaffusersController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminStaffusersController() {
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
        String adminId = request.getParameter("adminId");

        if (!password.equals(confirmPassword)) {
            // Passwords do not match
            request.setAttribute("errorMessage", "Passwords do not match!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffusers.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("INSERT INTO staff (username, password, email, phoneNumber, birthDate, gender, adminId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setDate(5, java.sql.Date.valueOf(birthDate));
            ps.setString(6, gender);
            if (adminId != null && !adminId.isEmpty()) {
                ps.setInt(7, Integer.parseInt(adminId));
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            int result = ps.executeUpdate();
            if (result > 0) {
                // Signup successful
                response.sendRedirect("login.jsp");
            } else {
                // Signup failed
                request.setAttribute("errorMessage", "An error occurred during signup. Please try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffusers.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) { // Catch SQLException specifically
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffusers.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) { // Catch other exceptions
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffusers.jsp");
            dispatcher.forward(request, response);
        }
    }
}

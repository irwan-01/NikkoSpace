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

@WebServlet("/AdminStaffusersController")
public class AdminStaffusersController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AdminStaffusersController() {
        super();
    }

    @Override
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
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffsignup.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection con = AzureSqlDatabaseConnection.getConnection()) {
            String sql = "INSERT INTO staff (username, password, email, phoneNumber, birthDate, gender, adminId) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, hashedPassword);
                ps.setString(3, email);
                ps.setString(4, phoneNumber);
                ps.setDate(5, birthDate != null && !birthDate.isEmpty() ? java.sql.Date.valueOf(birthDate) : null);
                ps.setString(6, gender);

                if (adminId != null && !adminId.isEmpty()) {
                    try {
                        ps.setInt(7, Integer.parseInt(adminId));
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "Admin ID must be a valid number.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffsignup.jsp");
                        dispatcher.forward(request, response);
                        return;
                    }
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
                    RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffsignup.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminStaffsignup.jsp");
            dispatcher.forward(request, response);
        }
    }
}

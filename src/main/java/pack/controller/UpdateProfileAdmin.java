package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pack.connection.AzureSqlDatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/UpdateProfileAdmin")
public class UpdateProfileAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProfileAdmin() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("loginAdmin.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE userId = ?")) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("username", rs.getString("username"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("phoneNumber", rs.getString("phoneNumber"));
                    request.setAttribute("birthDate", rs.getDate("birthDate"));
                    request.setAttribute("gender", rs.getString("gender"));
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileAdmin.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("loginAdmin.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        try (Connection con = AzureSqlDatabaseConnection.getConnection()) {
            // Validate the current password
            PreparedStatement ps = con.prepareStatement("SELECT password FROM staff WHERE userId = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Check if the current password matches the stored password
                if (!storedPassword.equals(currentPassword)) {
                    request.setAttribute("error", "Incorrect current password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileAdmin.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                // Check if new password matches confirm password
                if (!newPassword.equals(confirmPassword)) {
                    request.setAttribute("error", "New password and confirm password do not match.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileAdmin.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

                // Update the password along with other profile details
                PreparedStatement updatePs = con.prepareStatement(
                        "UPDATE staff SET username = ?, email = ?, phoneNumber = ?, birthDate = ?, gender = ?, password = ? WHERE userId = ?");
                updatePs.setString(1, username);
                updatePs.setString(2, email);
                updatePs.setString(3, phoneNumber);
                updatePs.setString(4, birthDate);
                updatePs.setString(5, gender);
                updatePs.setString(6, newPassword); // Update password
                updatePs.setInt(7, userId);

                int rowsUpdated = updatePs.executeUpdate();

                if (rowsUpdated > 0) {
                    response.sendRedirect("ProfileAdmin");
                } else {
                    request.setAttribute("error", "Failed to update profile.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileAdmin.jsp");
                    dispatcher.forward(request, response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}

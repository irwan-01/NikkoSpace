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

@WebServlet("/UpdateProfileController")
public class UpdateProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProfileController() {
        super();
    }

    // Display the user's current profile information in the form for editing
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            // Redirect to login if the user is not logged in
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE userId = ?")) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", rs.getString("username"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("phoneNumber", rs.getString("phoneNumber"));
                    request.setAttribute("birthDate", rs.getDate("birthDate"));
                    request.setAttribute("gender", rs.getString("gender"));
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfile.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    // Handle updating the profile information
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    HttpSession session = request.getSession(false);
    if (session == null || session.getAttribute("userId") == null) {
        // Redirect to login if the user is not logged in
        response.sendRedirect("login.jsp");
        return;
    }

    int userId = (int) session.getAttribute("userId");
    String username = request.getParameter("username");
    String email = request.getParameter("email");
    String phoneNumber = request.getParameter("phoneNumber");
    String birthDate = request.getParameter("birthDate");
    String gender = request.getParameter("gender");
    String password = request.getParameter("password");
    String confirmPassword = request.getParameter("confirmPassword");

    // Check if passwords match
    if (password != null && !password.isEmpty() && !password.equals(confirmPassword)) {
        request.setAttribute("error", "Passwords do not match.");
        RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfile.jsp");
        dispatcher.forward(request, response);
        return;
    }

    try (Connection con = AzureSqlDatabaseConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(
             "UPDATE users SET username = ?, email = ?, phoneNumber = ?, birthDate = ?, gender = ?" +
             (password != null && !password.isEmpty() ? ", password = ?" : "") + " WHERE userId = ?")) {

        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, phoneNumber);
        ps.setString(4, birthDate);
        ps.setString(5, gender);

        int paramIndex = 6;
        if (password != null && !password.isEmpty()) {
            ps.setString(paramIndex++, password); // Assuming passwords are stored in plain text (not recommended)
        }
        ps.setInt(paramIndex, userId);

        int rowsUpdated = ps.executeUpdate();

        if (rowsUpdated > 0) {
            response.sendRedirect("profile.jsp"); // Redirect to the profile page after successful update
        } else {
            request.setAttribute("error", "Failed to update profile");
            RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfile.jsp");
            dispatcher.forward(request, response);
        }

    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("error.jsp");
    }
}

}

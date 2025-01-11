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

@WebServlet("/UpdateProfileStaffController")
public class UpdateProfileStaffController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProfileStaffController() {
        super();
    }

    // Display staff profile for editing
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int staffId = (int) session.getAttribute("staffId");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE staffId = ?")) {

            ps.setInt(1, staffId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    request.setAttribute("username", rs.getString("username"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("phoneNumber", rs.getString("phoneNumber"));
                    request.setAttribute("birthDate", rs.getString("birthDate"));
                    request.setAttribute("gender", rs.getString("gender"));
                    request.setAttribute("created_at", rs.getTimestamp("created_at"));
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileStaff.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    // Handle profile updates
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int staffId = (int) session.getAttribute("staffId");
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
             PreparedStatement ps = con.prepareStatement("UPDATE staff SET username = ?, email = ?, phoneNumber = ?, birthDate = ?, gender = ? WHERE staffId = ?")) {

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, phoneNumber);
            ps.setString(4, birthDate);
            ps.setString(5, gender);
            

            int paramIndex = 6;
            if (password != null && !password.isEmpty()) {
                ps.setString(paramIndex++, password); // Assuming passwords are stored in plain text (not recommended)
            }
            ps.setInt(paramIndex, staffId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("ProfileStaffController"); // Redirect to the profile page after successful update
            } else {
                request.setAttribute("error", "Failed to update profile");
                RequestDispatcher dispatcher = request.getRequestDispatcher("updateProfileStaff.jsp");
                dispatcher.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

}

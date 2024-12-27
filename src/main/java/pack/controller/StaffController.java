package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import pack.connection.AzureSqlDatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/StaffController")
public class StaffController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("create".equals(action)) {
            createStaff(request, response);
        } else if ("view".equals(action)) {
            viewStaffProfile(request, response);
        } else if ("update".equals(action)) {
            updateStaffProfile(request, response);
        }
    }

    private void createStaff(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String phoneNumber = request.getParameter("phoneNumber");
        String birthDate = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String adminId = request.getParameter("adminId");

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("staff_signup.jsp");
            dispatcher.forward(request, response);
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO staff (username, password, email, phoneNumber, birthDate, gender, adminId) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, phoneNumber);
            ps.setDate(5, java.sql.Date.valueOf(birthDate));
            ps.setString(6, gender);
            ps.setObject(7, adminId != null && !adminId.isEmpty() ? Integer.parseInt(adminId) : null);

            int result = ps.executeUpdate();
            if (result > 0) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("errorMessage", "Failed to create staff. Try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("staff_signup.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("staff_signup.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void viewStaffProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("staffId") != null) {
            int staffId = (int) session.getAttribute("staffId");

            try (Connection con = AzureSqlDatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE staffId = ?")) {
                ps.setInt(1, staffId);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    request.setAttribute("username", rs.getString("username"));
                    request.setAttribute("email", rs.getString("email"));
                    request.setAttribute("phoneNumber", rs.getString("phoneNumber"));
                    request.setAttribute("birthDate", rs.getDate("birthDate"));
                    request.setAttribute("gender", rs.getString("gender"));
                    request.setAttribute("adminId", rs.getInt("adminId"));

                    RequestDispatcher dispatcher = request.getRequestDispatcher("staff_profile.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect("login.jsp");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    private void updateStaffProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Code to update the staff profile (similar to the createStaff logic, but using an UPDATE query)
    }
}

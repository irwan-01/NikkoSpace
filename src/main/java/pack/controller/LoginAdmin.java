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

@WebServlet("/LoginAdmin")
public class LoginAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginAdmin() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE username = ? AND password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password); // Consider hashing passwords for security

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Login successful
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("user_id", rs.getInt("user_id")); // Changed to user_id
                response.sendRedirect("ProfileAdmin");
            } else {
                // Login failed
                request.setAttribute("errorMessage", "Invalid username or password.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("loginAdmin.jsp");
                dispatcher.forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("loginAdmin.jsp");
            dispatcher.forward(request, response);
        }
    }
}

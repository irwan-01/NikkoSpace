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
import java.sql.SQLException;

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int user_id = rs.getInt("user_id"); // Changed userId to user_id
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    session.setAttribute("user_id", user_id);

                    // Update session_status to active
                    try (PreparedStatement sessionPs = con.prepareStatement(
                            "UPDATE users SET session_status = 'active' WHERE user_id = ?")) {
                        sessionPs.setInt(1, user_id);
                        sessionPs.executeUpdate();
                    }

                    response.sendRedirect("profile.jsp");
                } else {
                    // Invalid login
                    request.setAttribute("errorMessage", "Invalid username or password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                    dispatcher.forward(request, response);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer user_id = (Integer) session.getAttribute("user_id");
            if (user_id != null) {
                try (Connection con = AzureSqlDatabaseConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                             "UPDATE users SET session_status = 'inactive' WHERE user_id = ?")) {
                    ps.setInt(1, user_id);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                session.invalidate();
            }
        }
        response.sendRedirect("login.jsp");
    }
}

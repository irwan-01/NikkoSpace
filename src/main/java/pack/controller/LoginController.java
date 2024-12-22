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

@WebServlet("/LoginController")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate user credentials
        try (Connection con = AzureSqlDatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");

                    // Compare entered password with stored hashed password
                    if (BCrypt.checkpw(password, storedHashedPassword)) {
                        // Successful login
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        session.setAttribute("userId", rs.getInt("userId"));
                        response.sendRedirect("index.jsp"); // Redirect to the main page
                    } else {
                        // Invalid password
                        request.setAttribute("errorMessage", "Invalid username or password.");
                        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                        dispatcher.forward(request, response);
                    }
                } else {
                    // Username not found
                    request.setAttribute("errorMessage", "Invalid username or password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    }
}

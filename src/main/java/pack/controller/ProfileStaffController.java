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

@WebServlet("/ProfileStaffController")
public class ProfileStaffController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProfileStaffController() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("staffId") == null) {
            // Redirect to login if the staff is not logged in
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
                } else {
                    response.sendRedirect("error.jsp");
                    return;
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("profileStaff.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}

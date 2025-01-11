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

@WebServlet("/StaffLoginController")
public class StaffLoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public StaffLoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve staffId from the form
        String staffId = request.getParameter("staffId");

        try (Connection con = AzureSqlDatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM staff WHERE staffId = ?")) {

            ps.setInt(1, Integer.parseInt(staffId));
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Staff ID exists, redirect to staff page
                HttpSession session = request.getSession();
                session.setAttribute("staffId", staffId);
                session.setAttribute("username", rs.getString("username"));
                session.setAttribute("email", rs.getString("email"));
                session.setAttribute("phoneNumber", rs.getString("phoneNumber"));
                session.setAttribute("birthDate", rs.getDate("birthDate"));
                session.setAttribute("gender", rs.getString("gender"));
                session.setAttribute("adminId", rs.getInt("adminId"));

                response.sendRedirect("profileStaff.jsp");
            } else {
                // Staff ID does not exist
                request.setAttribute("errorMessage", "Invalid Staff ID. Please try again.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("staffLogin.jsp");
                dispatcher.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred. Please try again.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("staffLogin.jsp");
            dispatcher.forward(request, response);
        }
    }
}

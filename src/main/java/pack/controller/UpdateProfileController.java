
package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.connection.ConnectionManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import pack.model.Package;

/**
 * Servlet implementation class UpdateProfileController
 */
public class UpdateProfileController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateProfileController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

    if (id != null) {
        try {
            int packageId = Integer.parseInt(id);

            Connection con = pack.connection.AzureSqlDatabaseConnection.getConnection();

            String sql = "SELECT * FROM package WHERE packageId = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Profile pfl = new Profile(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getDouble("email")
                );
                request.setAttribute("pfl", pfl);
            }

            con.close();

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid profile ID format.");
        } catch (SQLException e) {
            System.out.println("Error retrieving profile: " + e.getMessage());
        }
    } else {
        System.out.println("Error: No profile ID provided.");
    }

    RequestDispatcher req = request.getRequestDispatcher("updateProfile.jsp");
    req.forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

    if (id != null) {
        try {
            int userId = Integer.parseInt(id);
            String username = request.getParameter("username");
            String email = request.getParameter("email");

            Connection con = pack.connection.AzureSqlDatabaseConnection.getConnection();

            // Corrected SQL query (changed "id" to "userId")
            String sql = "UPDATE package SET packageName = ?, packagePrice = ? WHERE packageId = ?"; 

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setInt(3, userId); 
            ps.executeUpdate();
            con.close();

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid profile ID or price format.");
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
    } else {
        System.out.println("Error: No profile ID provided.");
    }

    RequestDispatcher req = request.getRequestDispatcher("login.jsp"); 
    req.forward(request, response);
	
}
}

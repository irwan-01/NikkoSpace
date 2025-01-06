package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Servlet implementation class editPetController
 */
public class UpdateProfileController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public UpdateProfileController() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");

        if (userId != null) {
            try {
                int id = Integer.parseInt(userId);

                // Retrieve connection using ConnectionManager
                Connection con = pack.connection.AzureSqlDatabaseConnection.getConnection();

                // SQL query to fetch pet details by ID
                String sql = "SELECT * FROM users WHERE userId = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Profile profile = new Profile(
                        rs.getInt("userId"),
                        rs.getString("username"),
                        rs.getString("email")  // Use float as petWeight is of type FLOAT in SQL
                    );
                    // Set the pet data as an attribute
                    request.setAttribute("profile", profile);
                }

                con.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid user ID format.");
            } catch (SQLException e) {
                System.out.println("Error retrieving user: " + e.getMessage());
            }
        } else {
            System.out.println("Error: No user ID provided.");
        }

        // Forward the request to the updateProfile.jsp page for editing
        RequestDispatcher req = request.getRequestDispatcher("updateProfile.jsp");
        req.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String id = request.getParameter("id");

        if (id != null) {
            try {
                int userId = Integer.parseInt(id);
                String username = request.getParameter("username");
                String email = request.getParameter("email");

                Connection con = pack.connection.AzureSqlDatabaseConnection.getConnection();

                // Corrected SQL query (changed "id" to "packageId")
                String sql = "UPDATE users SET username = ?, email = ? WHERE userId = ?"; 

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.setInt(3, userId); 
                ps.executeUpdate();
                con.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid user ID or price format.");
            } catch (SQLException e) {
                System.out.println("Error updating profile: " + e.getMessage());
            }
        } else {
            System.out.println("Error: No user ID provided.");
        }

        RequestDispatcher req = request.getRequestDispatcher("profile.jsp"); 
        req.forward(request, response);
    }
}

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
 * Servlet implementation class ViewPackageController
 */
@WebServlet("/viewPackage") 
public class ViewPackageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ViewPackageController() {
        super();
    }

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
                    String packageName = rs.getString("packageName");
                    double packagePrice = rs.getDouble("packagePrice");

                    // Set the package name and price as attributes
                    request.setAttribute("packageName", packageName);
                    request.setAttribute("packagePrice", packagePrice);
                }

                con.close();

            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid package ID format.");
            } catch (SQLException e) {
                System.out.println("Error retrieving package: " + e.getMessage());
            }
        } else {
            System.out.println("Error: No package ID provided.");
        }

        RequestDispatcher req = request.getRequestDispatcher("viewPackage.jsp");
        req.forward(request, response);
    }
}

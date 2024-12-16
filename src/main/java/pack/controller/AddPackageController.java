package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import pack.connection.AzureSqlDatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * Servlet implementation class AddPackageController
 */
public class AddPackageController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public AddPackageController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String packageName = request.getParameter("packageName");
            // Check if packageName is null or empty
            if (packageName == null || packageName.trim().isEmpty()) {
                System.out.println("Error: Package name cannot be empty.");
                return;
            }

            String packagePriceStr = request.getParameter("packagePrice");
            // Check if packagePriceStr is null or empty
            if (packagePriceStr == null || packagePriceStr.trim().isEmpty()) {
                System.out.println("Error: Package price cannot be empty.");
                return;
            }
            double packagePrice = Double.parseDouble(packagePriceStr);

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection con = AzureSqlDatabaseConnection.getConnection();
            String sql = "INSERT INTO package (packageName, packagePrice) VALUES (?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, packageName);
            ps.setDouble(2, packagePrice);
            ps.executeUpdate();
            con.close();    

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid package price format.");
        } catch (SQLException e) {
            System.out.println("Error adding package: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e); 
        }

        RequestDispatcher req = request.getRequestDispatcher("index.jsp"); 
        req.forward(request, response);
    }
}

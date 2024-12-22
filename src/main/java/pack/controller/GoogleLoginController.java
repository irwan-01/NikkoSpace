package pack.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pack.connection.AzureSqlDatabaseConnection;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpResponseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.json.JSONObject;

@WebServlet("/GoogleLoginController")
public class GoogleLoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GoogleLoginController() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the ID token from the client
        String idToken = request.getParameter("idtoken");

        // Verify the ID token by calling Google's API
        String googleApiUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;
        HttpURLConnection connection = (HttpURLConnection) new URL(googleApiUrl).openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuffer responseBuffer = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                responseBuffer.append(inputLine);
            }

            // Parse the Google response
            JSONObject googleUser = new JSONObject(responseBuffer.toString());
            String googleUserId = googleUser.getString("sub");
            String googleEmail = googleUser.getString("email");
            String googleName = googleUser.getString("name");

            // Now you can store or check if the user exists in your database
            try (Connection con = AzureSqlDatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM users WHERE google_id = ?";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, googleUserId);
                    var rs = ps.executeQuery();

                    if (rs.next()) {
                        // User already exists
                        request.getSession().setAttribute("username", googleName);
                        request.getSession().setAttribute("userId", rs.getInt("userId"));
                        response.sendRedirect("index.jsp");
                    } else {
                        // User not found, create a new user in your database
                        String insertSql = "INSERT INTO users (google_id, email, name) VALUES (?, ?, ?)";
                        try (PreparedStatement insertPs = con.prepareStatement(insertSql)) {
                            insertPs.setString(1, googleUserId);
                            insertPs.setString(2, googleEmail);
                            insertPs.setString(3, googleName);
                            insertPs.executeUpdate();
                        }

                        // Redirect to the main page or profile page
                        response.sendRedirect("index.jsp");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp"); // Redirect to login page on error
        }
    }
}

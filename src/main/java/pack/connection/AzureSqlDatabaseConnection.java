package pack.connection; // Make sure this matches your package declaration

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AzureSqlDatabaseConnection {

    // Database credentials
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static final String DB_CONNECTION = "jdbc:sqlserver://nikkospace.database.windows.net:1433;database=Nikko Space;user=nikko@nikkospace;password=Muhammadyazid01;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private static final String DB_USER = "nikko@nikkospace"; 
    private static final String DB_PASSWORD = "Muhammadyazid01!";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(DB_CONNECTION);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

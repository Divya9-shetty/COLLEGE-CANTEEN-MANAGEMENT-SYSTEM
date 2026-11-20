package canteen;

import java.sql.Connection;
import java.sql.DriverManager;

// DatabaseHelper: Provides a static method to get a MySQL connection
public class DatabaseHelper {
    private static final String URL  = "jdbc:mysql://localhost:3306/canteen_db";
    private static final String USER = "root";
    private static final String PASS = ""; // Default XAMPP has no password

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            System.err.println("DB Connection failed: " + e.getMessage());
            return null;
        }
    }
}

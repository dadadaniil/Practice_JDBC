import java.sql.*;

public class DatabaseConnector {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database";
    public static final String FREQUENCIES_TABLE = "frequencies";
    public static final String COORDINATES_TABLE = "coordinates";

    private Connection conn;

    public DatabaseConnector() {
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("Error connecting to the database.", e);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public boolean isConnected() {
        try {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            throw new RuntimeException("Error checking connection status.", e);
        }
    }
}


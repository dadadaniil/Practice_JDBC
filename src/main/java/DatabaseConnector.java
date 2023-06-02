import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private Connection conn;

    private boolean establishConnection(String dbURL) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean connect() {
        return establishConnection(DB_URL);
    }

    public boolean connect(String dbName) {
        return establishConnection(DB_URL + dbName);
    }

    public boolean createDatabase(String dbName) {
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + dbName;
        return executeUpdate(createDatabaseQuery, "Error creating database.");
    }

    public boolean createTable(String tableName) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + tableName + " (id INT PRIMARY KEY, name VARCHAR(50));";
        return executeUpdate(createTableQuery, "Error creating table.");
    }

    private boolean executeUpdate(String query, String errorMessage) {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println(errorMessage);
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args) {
        DatabaseConnector dbConnector = new DatabaseConnector();

        if (dbConnector.connect()) {
            System.out.println("Connected to the server.");
            if (dbConnector.createDatabase("my_database")) {
                System.out.println("Database created successfully.");
                if (dbConnector.connect("my_database")) {
                    if (dbConnector.createTable("my_table")) {
                        System.out.println("Table created successfully.");
                    }
                }
            }
        } else {
            System.out.println("Failed to connect to the server.");
        }
    }
}
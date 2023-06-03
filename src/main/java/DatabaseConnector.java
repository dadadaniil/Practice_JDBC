import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector {
    private static final String USERNAME = "user";
    private static final String PASSWORD = "root";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/my_database";
    private static Connection conn;

    static {
        try {
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseConnector() throws SQLException {
    }

    public static void main(String[] args) {
        try {

            DatabaseConnector dbConnector = new DatabaseConnector();
            if (dbConnector.connect()) {
                System.out.println("Connected to the server.");
            }

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM coordinates");

            ArrayList<LenNumPair> listOfPairs = new ArrayList<>();

            while (rs.next()) {
                LenNumPair pair2 = new LenNumPair(rs.getDouble("x1"), rs.getDouble("x2"));

                boolean isPairFound = false;

                System.out.println(rs.getInt("x1") + " " + rs.getInt("x2") + " " + pair2);

                for (LenNumPair pair : listOfPairs) {
                    if (pair.getLen() == pair2.getLen()) {
                        isPairFound = true;
                        pair.setNum(pair.getNum() + 1);
                        break;
                    }
                }
                if (!isPairFound) {
                    listOfPairs.add(pair2);
                }

            }
            System.out.println();
            System.out.println(listOfPairs);
            System.out.println();

            String clearTableSQL = "DELETE FROM frequencies";
            stmt.executeUpdate(clearTableSQL);

            for (LenNumPair pair : listOfPairs) {
                String insertSQL = "INSERT INTO frequencies (len, num) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSQL);
                pstmt.setInt(1, pair.getLen());
                pstmt.setInt(2, pair.getNum());
                pstmt.executeUpdate();
            }

            ResultSet rs2=stmt.executeQuery("SELECT *FROM frequencies  WHERE len>num");
            while (rs2.next()){
                    System.out.println("len="+rs2.getInt("len")+" is greater then num="+rs2.getInt("num"));
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
}
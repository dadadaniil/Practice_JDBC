import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final String NUM = "num";
    private static final String LEN = "len";
    private static final String SELECT_COORDINATES_QUERY = "SELECT ABS(CEIL(x1 - x2)) AS len, COUNT(*) AS num FROM Coordinates GROUP BY len ORDER BY len ASC";
    private static final String DELETE_FREQUENCIES_QUERY = "DELETE FROM " + DatabaseConnector.FREQUENCIES_TABLE;
    private static final String INSERT_FREQUENCIES_QUERY = "INSERT INTO " + DatabaseConnector.FREQUENCIES_TABLE + " (" + LEN + ", " + NUM + ") VALUES ";
    private static final String SELECT_FREQUENCIES_QUERY = "SELECT * FROM " + DatabaseConnector.FREQUENCIES_TABLE + " WHERE len > num ORDER BY len ASC";

    public static void main(String[] args) {
        try {
            DatabaseConnector dbConnector = new DatabaseConnector();
            if (!dbConnector.isConnected()) {
                System.out.println("Failed to connect to the server.");
                return;
            }
            System.out.println("Connected to the server.");
            System.out.println();

            try (Connection conn = dbConnector.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet resultSet = stmt.executeQuery(SELECT_COORDINATES_QUERY)) {

                List<LenNumPair> pairsList = new ArrayList<>();

                while (resultSet.next()) {
                    LenNumPair newPair = new LenNumPair(resultSet.getInt(LEN), resultSet.getInt(NUM));
                    pairsList.add(newPair);
                    System.out.println(newPair);
                }

                System.out.println();
                stmt.executeUpdate(DELETE_FREQUENCIES_QUERY);

                StringBuilder insertQuery = new StringBuilder(INSERT_FREQUENCIES_QUERY);
                for (int i = 0; i < pairsList.size(); i++) {
                    insertQuery.append("(?, ?)");
                    if (i < pairsList.size() - 1) {
                        insertQuery.append(", ");
                    }
                }
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery.toString())) {
                    for (int i = 0; i < pairsList.size(); i++) {
                        LenNumPair pair = pairsList.get(i);
                        pstmt.setDouble(i * 2 + 1, pair.getLen());
                        pstmt.setDouble(i * 2 + 2, pair.getNum());
                    }
                    pstmt.executeUpdate();
                }

                try (ResultSet resultSet2 = stmt.executeQuery(SELECT_FREQUENCIES_QUERY)) {
                    while (resultSet2.next()) {
                        System.out.println(LEN + " = " + resultSet2.getInt(LEN) + " is greater than num = " + resultSet2.getInt(NUM));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing SQL.", e);
        }
    }
}

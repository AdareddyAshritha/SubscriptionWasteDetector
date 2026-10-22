import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("MySQL connected successfully!");
            System.out.println("Database: "
                    + con.getCatalog());
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            System.out.println(e.getMessage());
        }
    }
}

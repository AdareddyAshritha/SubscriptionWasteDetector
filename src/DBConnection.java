import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Connects Java to the MySQL database.
public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/subscription_waste";
    private static final String USER = "root";

    // Replace this with the MySQL root password you created.
    private static final String PASSWORD = "YOUR_PASSWORD";

    public static Connection getConnection()
            throws SQLException {
        return DriverManager.getConnection(
                URL, USER, PASSWORD);
    }
}

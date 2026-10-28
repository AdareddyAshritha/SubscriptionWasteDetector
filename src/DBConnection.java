import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/subscription_waste";
        String username = "root";
        String password = "MYSQL_PASSWORD";

        Connection con = DriverManager.getConnection(url, username, password);

        return con;
    }
}
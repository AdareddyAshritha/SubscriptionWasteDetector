import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Handles subscription data using MySQL.
public class FileHandler {

    // Loads subscriptions from MySQL.
    public static ArrayList<Subscription> load() {
        ArrayList<Subscription> list = new ArrayList<>();

        String sql = "SELECT * FROM subscriptions ORDER BY id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double cost = rs.getDouble("cost");
                BillingCycle cycle = BillingCycle.valueOf(
                        rs.getString("billing_cycle"));
                LocalDate date = rs.getDate(
                        "last_used_date").toLocalDate();

                list.add(new Subscription(
                        id, name, cost, cycle, date));
            }

        } catch (SQLException e) {
            System.out.println(
                    "Could not load database data.");
            e.printStackTrace();
            return list;
        }

        // Import the old file if the database is empty.
        if (list.isEmpty()) {
            list = loadFromFile();

            if (!list.isEmpty()) {
                save(list);
            }
        }

        return list;
    }

    // Saves the complete list to MySQL.
    public static void save(ArrayList<Subscription> list) {

        String deleteSql = "DELETE FROM subscriptions";

        String insertSql =
                "INSERT INTO subscriptions "
                        + "(id, name, cost, billing_cycle, last_used_date) "
                        + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement delete =
                         con.prepareStatement(deleteSql)) {
                delete.executeUpdate();
            }

            try (PreparedStatement insert =
                         con.prepareStatement(insertSql)) {

                for (Subscription s : list) {
                    insert.setInt(1, s.getId());
                    insert.setString(2, s.getName());
                    insert.setDouble(3, s.getCost());
                    insert.setString(
                            4, s.getBillingCycle().name());
                    insert.setDate(
                            5, java.sql.Date.valueOf(
                                    s.getLastUsedDate()));

                    insert.addBatch();
                }

                insert.executeBatch();
            }

            con.commit();

        } catch (SQLException e) {
            System.out.println(
                    "Could not save database data.");
            e.printStackTrace();
        }
    }

    // Reads the old text file for the first import.
    private static ArrayList<Subscription> loadFromFile() {
        ArrayList<Subscription> list = new ArrayList<>();
        File file = new File("subscriptions.txt");

        if (!file.exists()) {
            return list;
        }

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                if (parts.length != 5) {
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double cost = Double.parseDouble(parts[2]);
                    BillingCycle cycle =
                            BillingCycle.valueOf(parts[3]);
                    LocalDate date =
                            LocalDate.parse(parts[4]);

                    if (id > 0 && !name.trim().isEmpty()
                            && Double.isFinite(cost) && cost >= 0
                            && !date.isAfter(LocalDate.now())) {

                        list.add(new Subscription(
                                id, name, cost, cycle, date));
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Skipping an invalid saved record.");
                }
            }

        } catch (IOException e) {
            System.out.println(
                    "Could not read the old text file.");
        }

        return list;
    }
}
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Performs subscription operations using the MySQL database.
public class SubscriptionService {

    // Adds a new subscription to the database.
    public void addSubscription(Subscription subscription)
            throws SQLException {

        String sql = "INSERT INTO subscriptions "
                + "(name, cost, billing_cycle, last_used_date) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subscription.getName());
            statement.setDouble(2, subscription.getCost());
            statement.setString(3, subscription.getBillingCycle().name());
            statement.setDate(4, Date.valueOf(subscription.getLastUsedDate()));

            statement.executeUpdate();
        }
    }

    // Retrieves all subscriptions from the database.
    public ArrayList<Subscription> getAllSubscriptions()
            throws SQLException {

        ArrayList<Subscription> subscriptions = new ArrayList<>();

        String sql = "SELECT id, name, cost, billing_cycle, last_used_date "
                + "FROM subscriptions ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                subscriptions.add(readSubscription(result));
            }
        }

        return subscriptions;
    }

    // Calculates the total monthly cost of all subscriptions.
    public double getTotalMonthlyCost() throws SQLException {
        double total = 0.0;
        ArrayList<Subscription> subscriptions = getAllSubscriptions();

        for (Subscription subscription : subscriptions) {
            total += subscription.getMonthlyCost();
        }

        return total;
    }

    // Estimates yearly spending using the monthly total.
    public double getYearlySpending() throws SQLException {
        return getTotalMonthlyCost() * 12.0;
    }

    // Finds subscriptions unused for the specified number of days.
    public ArrayList<Subscription> getUnusedSubscriptions(int days)
            throws SQLException {

        ArrayList<Subscription> unused = new ArrayList<>();
        ArrayList<Subscription> subscriptions = getAllSubscriptions();

        for (Subscription subscription : subscriptions) {
            if (subscription.isUnused(days)) {
                unused.add(subscription);
            }
        }

        return unused;
    }

    // Searches for subscription names containing the given text.
    public ArrayList<Subscription> searchSubscriptions(String searchText)
            throws SQLException {

        ArrayList<Subscription> matches = new ArrayList<>();

        String sql = "SELECT id, name, cost, billing_cycle, last_used_date "
                + "FROM subscriptions WHERE name LIKE ? ORDER BY id";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchText + "%");

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    matches.add(readSubscription(result));
                }
            }
        }

        return matches;
    }

    // Updates an existing subscription using its ID.
    public boolean updateSubscription(Subscription subscription)
            throws SQLException {

        String sql = "UPDATE subscriptions "
                + "SET name = ?, cost = ?, billing_cycle = ?, last_used_date = ? "
                + "WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, subscription.getName());
            statement.setDouble(2, subscription.getCost());
            statement.setString(3, subscription.getBillingCycle().name());
            statement.setDate(4, Date.valueOf(subscription.getLastUsedDate()));
            statement.setInt(5, subscription.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // Deletes a subscription using its ID.
    public boolean deleteSubscription(int id) throws SQLException {
        String sql = "DELETE FROM subscriptions WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    // Estimates monthly savings from unused subscriptions.
    public double getPotentialMonthlySavings(int days) throws SQLException {
        double savings = 0.0;
        ArrayList<Subscription> unused = getUnusedSubscriptions(days);

        for (Subscription subscription : unused) {
            savings += subscription.getMonthlyCost();
        }

        return savings;
    }

    // Converts a database row into a Subscription object.
    private Subscription readSubscription(ResultSet result)
            throws SQLException {

        return new Subscription(
                result.getInt("id"),
                result.getString("name"),
                result.getDouble("cost"),
                BillingCycle.valueOf(
                        result.getString("billing_cycle").toUpperCase()),
                result.getDate("last_used_date").toLocalDate()
        );
    }
}
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Main class that runs the Subscription Waste Detector.
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SubscriptionService service =
            new SubscriptionService();

    public static void main(String[] args) {
        System.out.println("===== Subscription Waste Detector =====");

        try {
            // Check whether Java can connect to MySQL.
            DBConnection.getConnection().close();
            System.out.println("Database connected successfully.");

            int choice;

            // Repeat the menu until the user selects Exit.
            do {
                showMenu();
                choice = readInteger("Enter your choice: ");

                try {
                    switch (choice) {
                        case 1:
                            addSubscription();
                            break;

                        case 2:
                            showSubscriptions(service.getAllSubscriptions());
                            break;

                        case 3:
                            System.out.printf(
                                    "Total monthly cost: Rs. %.2f%n",
                                    service.getTotalMonthlyCost());
                            break;

                        case 4:
                            findUnusedSubscriptions();
                            break;

                        case 5:
                            updateSubscription();
                            break;

                        case 6:
                            deleteSubscription();
                            break;

                        case 7:
                            searchSubscription();
                            break;

                        case 8:
                            showPotentialSavings();
                            break;

                        case 9:
                            System.out.printf(
                                    "Estimated yearly spending: Rs. %.2f%n",
                                    service.getYearlySpending());
                            break;

                        case 0:
                            System.out.println("Thank you for using the program.");
                            break;

                        default:
                            System.out.println(
                                    "Invalid choice. Enter a number from 0 to 9.");
                    }
                } catch (SQLException e) {
                    System.out.println(
                            "Database operation failed: " + e.getMessage());
                }

            } while (choice != 0);

        } catch (SQLException e) {
            System.out.println("Could not connect to the database.");
            System.out.println(e.getMessage());
        }
    }

    // Displays all menu options.
    private static void showMenu() {
        System.out.println();
        System.out.println("1. Add Subscription");
        System.out.println("2. View Subscriptions");
        System.out.println("3. Calculate Total Monthly Cost");
        System.out.println("4. Find Unused Subscriptions");
        System.out.println("5. Update Subscription");
        System.out.println("6. Delete Subscription");
        System.out.println("7. Search Subscription");
        System.out.println("8. Calculate Potential Monthly Savings");
        System.out.println("9. Calculate Estimated Yearly Spending");
        System.out.println("0. Exit");
    }

    // Accepts details and adds a subscription.
    private static void addSubscription() throws SQLException {
        String name = readName("Enter subscription name: ");
        double cost = readCost("Enter subscription cost: ");
        BillingCycle cycle = readBillingCycle();
        LocalDate lastUsed =
                readDate("Enter last used date (YYYY-MM-DD): ");

        Subscription subscription =
                new Subscription(0, name, cost, cycle, lastUsed);

        service.addSubscription(subscription);
        System.out.println("Subscription added successfully.");
    }

    // Displays subscriptions unused for a chosen number of days.
    private static void findUnusedSubscriptions() throws SQLException {
        int days = readPositiveInteger(
                "Find subscriptions unused for how many days? ");

        ArrayList<Subscription> unused =
                service.getUnusedSubscriptions(days);

        if (unused.isEmpty()) {
            System.out.println("No unused subscriptions found.");
        } else {
            showSubscriptions(unused);
        }
    }

    // Accepts new details and updates an existing subscription.
    private static void updateSubscription() throws SQLException {
        int id = readPositiveInteger("Enter subscription ID to update: ");

        ArrayList<Subscription> all = service.getAllSubscriptions();
        Subscription existing = findById(all, id);

        if (existing == null) {
            System.out.println("Subscription not found.");
            return;
        }

        System.out.println("Current details: " + existing);

        String name = readName("Enter new name: ");
        double cost = readCost("Enter new cost: ");
        BillingCycle cycle = readBillingCycle();
        LocalDate lastUsed =
                readDate("Enter last used date (YYYY-MM-DD): ");

        existing.setName(name);
        existing.setCost(cost);
        existing.setBillingCycle(cycle);
        existing.setLastUsedDate(lastUsed);

        if (service.updateSubscription(existing)) {
            System.out.println("Subscription updated successfully.");
        } else {
            System.out.println("Subscription was not updated.");
        }
    }

    // Deletes a subscription by ID.
    private static void deleteSubscription() throws SQLException {
        int id = readPositiveInteger("Enter subscription ID to delete: ");

        if (service.deleteSubscription(id)) {
            System.out.println("Subscription deleted successfully.");
        } else {
            System.out.println("Subscription not found.");
        }
    }

    // Searches subscriptions by name.
    private static void searchSubscription() throws SQLException {
        System.out.print("Enter subscription name to search: ");
        String text = scanner.nextLine().trim();

        if (text.isEmpty()) {
            System.out.println("Search text cannot be empty.");
            return;
        }

        ArrayList<Subscription> matches =
                service.searchSubscriptions(text);

        showSubscriptions(matches);
    }

    // Displays estimated savings from unused subscriptions.
    private static void showPotentialSavings() throws SQLException {
        int days = readPositiveInteger(
                "Consider unused after how many days? ");

        double savings =
                service.getPotentialMonthlySavings(days);

        System.out.printf(
                "Potential monthly savings: Rs. %.2f%n", savings);

        System.out.println(
                "This is an estimate, not an automatic cancellation.");
    }

    // Prints subscription details and monthly equivalent costs.
    private static void showSubscriptions(
            ArrayList<Subscription> subscriptions) {

        if (subscriptions.isEmpty()) {
            System.out.println("No subscriptions found.");
            return;
        }

        for (Subscription subscription : subscriptions) {
            System.out.println(subscription);

            System.out.printf(
                    "Monthly equivalent: Rs. %.2f%n",
                    subscription.getMonthlyCost());
        }
    }

    // Finds a subscription in a list using its ID.
    private static Subscription findById(
            ArrayList<Subscription> subscriptions, int id) {

        for (Subscription subscription : subscriptions) {
            if (subscription.getId() == id) {
                return subscription;
            }
        }

        return null;
    }

    // Accepts a non-empty name.
    private static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = scanner.nextLine().trim();

            if (!name.isEmpty()) {
                return name;
            }

            System.out.println("Name cannot be empty.");
        }
    }

    // Accepts a valid, non-negative cost.
    private static double readCost(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            try {
                double cost = Double.parseDouble(text);

                if (Double.isFinite(cost) && cost >= 0) {
                    return cost;
                }

                System.out.println("Enter a valid non-negative cost.");

            } catch (NumberFormatException e) {
                System.out.println("Enter a number, such as 199.00.");
            }
        }
    }

    // Accepts an integer from the user.
    private static int readInteger(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Enter a whole number.");
            }
        }
    }

    // Accepts only positive integers.
    private static int readPositiveInteger(String prompt) {
        while (true) {
            int number = readInteger(prompt);

            if (number > 0) {
                return number;
            }

            System.out.println("Enter a number greater than zero.");
        }
    }

    // Accepts MONTHLY or YEARLY as the billing cycle.
    private static BillingCycle readBillingCycle() {
        while (true) {
            System.out.print("Enter billing cycle (MONTHLY/YEARLY): ");
            String text = scanner.nextLine().trim().toUpperCase();

            if (text.equals("MONTHLY")) {
                return BillingCycle.MONTHLY;
            }

            if (text.equals("YEARLY")) {
                return BillingCycle.YEARLY;
            }

            System.out.println("Enter MONTHLY or YEARLY.");
        }
    }

    // Accepts a valid date that is not in the future.
    private static LocalDate readDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String text = scanner.nextLine().trim();

            try {
                LocalDate date = LocalDate.parse(text);

                if (!date.isAfter(LocalDate.now())) {
                    return date;
                }

                System.out.println("Date cannot be in the future.");

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Use YYYY-MM-DD format, such as 2026-10-10.");
            }
        }
    }
}
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Scanner reads input entered by the user
        Scanner sc = new Scanner(System.in);

        // Create the service object to manage subscriptions
        SubscriptionService service = new SubscriptionService();

        // Stores the menu option selected by the user
        int choice = -1;

        // Repeat the menu until the user enters 0
        while (choice != 0) {

            // Display the main menu
            System.out.println("\n===== SUBSCRIPTION WASTE DETECTOR =====");
            System.out.println("1. Add Subscription");
            System.out.println("2. View Subscriptions");
            System.out.println("3. Total Monthly Cost");
            System.out.println("4. Find Unused Subscriptions");
            System.out.println("5. Update Subscription");
            System.out.println("6. Delete Subscription");
            System.out.println("7. Search Subscription");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                // Read the choice and convert it to an integer
                choice = Integer.parseInt(sc.nextLine().trim());

                // Perform the operation selected by the user
                switch (choice) {

                    case 1:
                        addSubscription(sc, service);
                        break;

                    case 2:
                        viewSubscriptions(service);
                        break;

                    case 3:
                        // Display the total monthly expense
                        System.out.printf(
                                "Total monthly cost: %.2f%n",
                                service.getTotalMonthlyCost()
                        );
                        break;

                    case 4:
                        findUnusedSubscriptions(sc, service);
                        break;

                    case 5:
                        updateSubscription(sc, service);
                        break;

                    case 6:
                        deleteSubscription(sc, service);
                        break;

                    case 7:
                        searchSubscriptions(sc, service);
                        break;

                    case 0:
                        // Display a message before exiting
                        System.out.println(
                                "Thank you for using the application."
                        );
                        break;

                    default:
                        // Handle menu choices outside the available options
                        System.out.println("Invalid choice. Try again.");
                }

            } catch (NumberFormatException e) {
                // Handle input that cannot be converted to an integer
                System.out.println("Please enter a valid number.");
            }
        }

        // Close the Scanner after the application ends
        sc.close();
    }

    // Add a new subscription
    private static void addSubscription(
            Scanner sc, SubscriptionService service) {

        try {
            // Read the subscription name
            System.out.print("Enter subscription name: ");
            String name = sc.nextLine().trim();

            // Reject an empty name
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            // Read the subscription cost
            System.out.print("Enter subscription cost: ");
            double cost = Double.parseDouble(sc.nextLine().trim());

            // Reject negative, infinite, or invalid numeric costs
            if (!Double.isFinite(cost) || cost < 0) {
                System.out.println(
                        "Cost must be a valid non-negative number."
                );
                return;
            }

            // Read and convert the billing cycle to an enum value
            System.out.print("Enter billing cycle (MONTHLY/YEARLY): ");
            BillingCycle cycle = BillingCycle.valueOf(
                    sc.nextLine().trim().toUpperCase()
            );

            // Read the last-used date in YYYY-MM-DD format
            System.out.print("Enter last used date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            // Do not allow a future last-used date
            if (date.isAfter(LocalDate.now())) {
                System.out.println("Date cannot be in the future.");
                return;
            }

            // Create the subscription object; the service assigns its ID
            Subscription s = new Subscription(
                    0, name, cost, cycle, date
            );

            // Add the subscription and save it
            service.addSubscription(s);
            System.out.println("Subscription added successfully.");

        } catch (NumberFormatException e) {
            // Handle a cost that is not a valid number
            System.out.println("Invalid cost.");

        } catch (DateTimeParseException e) {
            // Handle a date that does not follow the required format
            System.out.println("Invalid date. Use YYYY-MM-DD.");

        } catch (IllegalArgumentException e) {
            // Handle a billing cycle that is not MONTHLY or YEARLY
            System.out.println(
                    "Invalid billing cycle. Use MONTHLY or YEARLY."
            );
        }
    }

    // Display all saved subscriptions
    private static void viewSubscriptions(SubscriptionService service) {

        // Get the current subscription list
        ArrayList<Subscription> list = service.getAllSubscriptions();

        // Check whether the list is empty
        if (list.isEmpty()) {
            System.out.println("No subscriptions found.");
            return;
        }

        // Print each subscription
        for (Subscription s : list) {
            System.out.println(s);
        }
    }

    // Find subscriptions that have been unused for a chosen number of days
    private static void findUnusedSubscriptions(
            Scanner sc, SubscriptionService service) {

        try {
            // Ask the user for the unused-day threshold
            System.out.print("Enter number of unused days: ");
            int days = Integer.parseInt(sc.nextLine().trim());

            // Reject a negative threshold
            if (days < 0) {
                System.out.println("Days cannot be negative.");
                return;
            }

            // Get subscriptions that meet the threshold
            ArrayList<Subscription> unused =
                    service.getUnusedSubscriptions(days);

            // Check whether any unused subscriptions were found
            if (unused.isEmpty()) {
                System.out.println("No unused subscriptions found.");
                return;
            }

            System.out.println("Unused subscriptions:");

            // Display each unused subscription
            for (Subscription s : unused) {
                System.out.println(s);
            }

        } catch (NumberFormatException e) {
            // Handle input that is not a whole number
            System.out.println("Please enter a valid whole number.");
        }
    }

    // Search for subscriptions by name
    private static void searchSubscriptions(
            Scanner sc, SubscriptionService service) {

        // Read the name or part of the name to search for
        System.out.print("Enter subscription name to search: ");
        String keyword = sc.nextLine().trim();

        // Reject an empty search
        if (keyword.isEmpty()) {
            System.out.println("Search name cannot be empty.");
            return;
        }

        // Get matching subscriptions
        ArrayList<Subscription> results =
                service.searchSubscriptions(keyword);

        // Display a message if no match exists
        if (results.isEmpty()) {
            System.out.println("No matching subscriptions found.");
            return;
        }

        System.out.println("Matching subscriptions:");

        // Print all matching subscriptions
        for (Subscription s : results) {
            System.out.println(s);
        }
    }

    // Update the details of an existing subscription
    private static void updateSubscription(
            Scanner sc, SubscriptionService service) {

        try {
            // Read the ID of the subscription to update
            System.out.print("Enter subscription ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            // Initially, no matching subscription has been found
            Subscription existing = null;

            // Get all subscriptions
            ArrayList<Subscription> list = service.getAllSubscriptions();

            // Search for the subscription with the entered ID
            for (Subscription s : list) {
                if (s.getId() == id) {
                    existing = s;
                    break;
                }
            }

            // Stop if the ID does not exist
            if (existing == null) {
                System.out.println("Subscription not found.");
                return;
            }

            // Read the replacement name
            System.out.print("Enter new subscription name: ");
            String name = sc.nextLine().trim();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            // Read the new cost
            System.out.print("Enter new cost: ");
            double cost = Double.parseDouble(sc.nextLine().trim());

            if (!Double.isFinite(cost) || cost < 0) {
                System.out.println(
                        "Cost must be a valid non-negative number."
                );
                return;
            }

            // Read the new billing cycle
            System.out.print("Enter new billing cycle (MONTHLY/YEARLY): ");
            BillingCycle cycle = BillingCycle.valueOf(
                    sc.nextLine().trim().toUpperCase()
            );

            // Read the new last-used date
            System.out.print("Enter new last used date (YYYY-MM-DD): ");
            LocalDate date = LocalDate.parse(sc.nextLine().trim());

            // Reject future dates
            if (date.isAfter(LocalDate.now())) {
                System.out.println("Date cannot be in the future.");
                return;
            }

            // Create an updated object using the same ID
            Subscription updated = new Subscription(
                    id, name, cost, cycle, date
            );

            // Update the subscription and display the result
            if (service.updateSubscription(updated)) {
                System.out.println("Subscription updated successfully.");
            } else {
                System.out.println("Subscription not found.");
            }

        } catch (NumberFormatException e) {
            // Handle an invalid ID or cost
            System.out.println("Invalid ID or cost.");

        } catch (DateTimeParseException e) {
            // Handle an incorrectly formatted date
            System.out.println("Invalid date. Use YYYY-MM-DD.");

        } catch (IllegalArgumentException e) {
            // Handle an invalid billing cycle
            System.out.println(
                    "Invalid billing cycle. Use MONTHLY or YEARLY."
            );
        }
    }

    // Delete a subscription using its ID
    private static void deleteSubscription(
            Scanner sc, SubscriptionService service) {

        try {
            // Read the ID of the subscription to delete
            System.out.print("Enter subscription ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());

            // Delete the subscription and display the result
            if (service.deleteSubscription(id)) {
                System.out.println("Subscription deleted successfully.");
            } else {
                System.out.println("Subscription not found.");
            }

        } catch (NumberFormatException e) {
            // Handle an ID that is not a valid integer
            System.out.println("Please enter a valid ID.");
        }
    }
}
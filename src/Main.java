import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

// Starts the Subscription Waste Detector application.
public class Main {

    public static void main(String[] args) {

        SubscriptionService service = new SubscriptionService();
        Scanner sc = new Scanner(System.in);
        int choice = -1;

        // Repeats the menu until the user chooses Exit.
        while (choice != 0) {

            System.out.println("\n=== Subscription Waste Detector ===");
            System.out.println("1. Add subscription");
            System.out.println("2. View all subscriptions");
            System.out.println("3. Show total monthly cost");
            System.out.println("4. Show unused subscriptions");
            System.out.println("5. Update subscription");
            System.out.println("6. Delete subscription");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                // Option 1: Add a subscription.
                if (choice == 1) {

                    System.out.print("Name: ");
                    String name = sc.nextLine().trim();

                    if (name.isEmpty()) {
                        System.out.println("Name cannot be empty.");
                        continue;
                    }

                    System.out.print("Cost: ");
                    double cost = Double.parseDouble(sc.nextLine());

                    if (!Double.isFinite(cost) || cost < 0) {
                        System.out.println(
                                "Enter a valid non-negative cost.");
                        continue;
                    }

                    System.out.print(
                            "Billing cycle (MONTHLY/YEARLY): ");
                    BillingCycle cycle = BillingCycle.valueOf(
                            sc.nextLine().trim().toUpperCase());

                    System.out.print(
                            "Last used date (YYYY-MM-DD): ");
                    LocalDate lastUsed =
                            LocalDate.parse(sc.nextLine().trim());

                    if (lastUsed.isAfter(LocalDate.now())) {
                        System.out.println(
                                "Date cannot be in the future.");
                        continue;
                    }

                    service.addSubscription(
                            name, cost, cycle, lastUsed);

                    System.out.println(
                            "Subscription added successfully.");

                    // Option 2: View all subscriptions.
                } else if (choice == 2) {

                    if (service.getAll().isEmpty()) {
                        System.out.println("No subscriptions yet.");
                    } else {
                        for (Subscription s : service.getAll()) {
                            System.out.println(s);
                        }
                    }

                    // Option 3: Calculate monthly expenses.
                } else if (choice == 3) {

                    System.out.printf(
                            "Total monthly cost: %.2f%n",
                            service.getTotalMonthlyCost());

                    // Option 4: Find unused subscriptions.
                } else if (choice == 4) {

                    ArrayList<Subscription> unused =
                            service.getUnusedSubscriptions(30);

                    if (unused.isEmpty()) {
                        System.out.println(
                                "No unused subscriptions.");
                    } else {
                        for (Subscription s : unused) {
                            System.out.println(
                                    s + " | Unused for "
                                            + s.getDaysSinceLastUse()
                                            + " days");
                        }
                    }

                    // Option 5: Update a subscription.
                } else if (choice == 5) {

                    System.out.print("Enter subscription ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("New name: ");
                    String name = sc.nextLine().trim();

                    if (name.isEmpty()) {
                        System.out.println(
                                "Name cannot be empty.");
                        continue;
                    }

                    System.out.print("New cost: ");
                    double cost = Double.parseDouble(sc.nextLine());

                    if (!Double.isFinite(cost) || cost < 0) {
                        System.out.println(
                                "Enter a valid non-negative cost.");
                        continue;
                    }

                    System.out.print(
                            "Billing cycle (MONTHLY/YEARLY): ");
                    BillingCycle cycle = BillingCycle.valueOf(
                            sc.nextLine().trim().toUpperCase());

                    System.out.print(
                            "Last used date (YYYY-MM-DD): ");
                    LocalDate lastUsed =
                            LocalDate.parse(sc.nextLine().trim());

                    if (lastUsed.isAfter(LocalDate.now())) {
                        System.out.println(
                                "Date cannot be in the future.");
                        continue;
                    }

                    if (service.updateSubscription(
                            id, name, cost, cycle, lastUsed)) {
                        System.out.println(
                                "Subscription updated successfully.");
                    } else {
                        System.out.println(
                                "Subscription ID not found.");
                    }

                    // Option 6: Delete a subscription.
                } else if (choice == 6) {

                    System.out.print(
                            "Enter subscription ID to delete: ");
                    int id = Integer.parseInt(sc.nextLine());

                    if (service.deleteSubscription(id)) {
                        System.out.println(
                                "Subscription deleted successfully.");
                    } else {
                        System.out.println(
                                "Subscription ID not found.");
                    }

                    // Handles invalid menu choices.
                } else if (choice != 0) {
                    System.out.println("Invalid menu choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid number.");
                choice = -1;

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Invalid date. Use YYYY-MM-DD.");
                choice = -1;

            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Invalid billing cycle or input.");
                choice = -1;
            }
        }

        System.out.println("Goodbye!");
        sc.close();
    }
}
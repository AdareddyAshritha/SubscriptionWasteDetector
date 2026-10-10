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
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                // Adds a new subscription.
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
                        System.out.println("Enter a valid non-negative cost.");
                        continue;
                    }

                    System.out.print("Billing cycle (MONTHLY/YEARLY): ");
                    BillingCycle cycle = BillingCycle.valueOf(
                            sc.nextLine().trim().toUpperCase());

                    System.out.print("Last used date (YYYY-MM-DD): ");
                    LocalDate lastUsed = LocalDate.parse(sc.nextLine());

                    // Prevents a future date from being entered.
                    if (lastUsed.isAfter(LocalDate.now())) {
                        System.out.println("Date cannot be in the future.");
                        continue;
                    }

                    service.addSubscription(name, cost, cycle, lastUsed);
                    System.out.println("Subscription added successfully.");

                    // Displays all saved subscriptions.
                } else if (choice == 2) {
                    if (service.getAll().isEmpty()) {
                        System.out.println("No subscriptions yet.");
                    } else {
                        for (Subscription s : service.getAll()) {
                            System.out.println(s);
                        }
                    }

                    // Displays the total monthly expense.
                } else if (choice == 3) {
                    System.out.printf("Total monthly cost: %.2f%n",
                            service.getTotalMonthlyCost());

                    // Displays subscriptions unused for 30 days or more.
                } else if (choice == 4) {
                    ArrayList<Subscription> unused =
                            service.getUnusedSubscriptions(30);

                    if (unused.isEmpty()) {
                        System.out.println("No unused subscriptions.");
                    } else {
                        for (Subscription s : unused) {
                            System.out.println(s + " | Unused for "
                                    + s.getDaysSinceLastUse() + " days");
                        }
                    }

                } else if (choice != 0) {
                    System.out.println("Invalid menu choice.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                choice = -1;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid date. Use YYYY-MM-DD.");
                choice = -1;

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid billing cycle or input.");
                choice = -1;
            }
        }

        System.out.println("Goodbye!");
        sc.close();
    }
}
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Create a few subscriptions to test
        Subscription s1 = new Subscription(
                1, "Netflix", 15.49,
                BillingCycle.MONTHLY,
                LocalDate.of(2026, 9, 28)
        );

        Subscription s2 = new Subscription(
                2, "Cloud Storage", 120,
                BillingCycle.YEARLY,
                LocalDate.of(2026, 5, 2)
        );

        Subscription s3 = new Subscription(
                3, "Spotify", 9.99,
                BillingCycle.MONTHLY,
                LocalDate.of(2025, 12, 1)
        );

        // Store them in an array
        Subscription[] subs = { s1, s2, s3 };

        // Print details of each subscription
        for (Subscription s : subs) {
            System.out.println(s);
            System.out.println("Monthly cost: " + s.getMonthlyCost());

            if (s.isUnused(30)) {
                System.out.println("Waste: unused for "
                        + s.getDaysSinceLastUse() + " days");
            }

            System.out.println();
        }

        // Total monthly cost
        double total = 0;
        for (Subscription s : subs) {
            total += s.getMonthlyCost();
        }

        System.out.println("Total monthly cost: " + total);
    }
}

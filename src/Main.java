import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        // Create subscriptions for testing
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

        // Display subscription details
        System.out.println("Subscription details:");
        System.out.println(s1);
        System.out.println(s2);

        // Display monthly cost
        System.out.println("Monthly cost of "
                + s1.getName() + ": " + s1.getMonthlyCost());

        System.out.println("Monthly cost of "
                + s2.getName() + ": " + s2.getMonthlyCost());
    }
}

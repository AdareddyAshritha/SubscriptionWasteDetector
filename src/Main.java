public class Main {

    public static void main(String[] args) {

        // Create a subscription object
        Subscription s1 = new Subscription(
                1, "Netflix", 15.49, "Monthly"
        );

        // Display subscription details
        System.out.println("Subscription details:");
        System.out.println(s1);
    }
}

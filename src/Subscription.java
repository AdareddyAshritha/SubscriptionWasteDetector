public class Subscription {

    // Variables to store subscription details
    private int id;
    private String name;
    private double cost;
    private String billingCycle;

    // Constructor to initialize subscription details
    public Subscription(int id, String name, double cost, String billingCycle) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.billingCycle = billingCycle;
    }

    // Get the subscription ID
    public int getId() {
        return id;
    }

    // Get the subscription name
    public String getName() {
        return name;
    }

    // Get the subscription cost
    public double getCost() {
        return cost;
    }

    // Get the billing cycle
    public String getBillingCycle() {
        return billingCycle;
    }

    // Display subscription details
    public String toString() {
        return id + " | " + name + " | " + cost + " | " + billingCycle;
    }
}

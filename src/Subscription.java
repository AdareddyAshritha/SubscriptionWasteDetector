import java.time.LocalDate;

public class Subscription {

    // Subscription details
    private int id;
    private String name;
    private double cost;
    private BillingCycle billingCycle;
    private LocalDate lastUsedDate;

    // Constructor
    public Subscription(int id, String name, double cost,
                        BillingCycle billingCycle, LocalDate lastUsedDate) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.billingCycle = billingCycle;
        this.lastUsedDate = lastUsedDate;
    }

    // Get subscription ID
    public int getId() {
        return id;
    }

    // Get subscription name
    public String getName() {
        return name;
    }

    // Get subscription cost
    public double getCost() {
        return cost;
    }

    // Get billing cycle
    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    // Get last used date
    public LocalDate getLastUsedDate() {
        return lastUsedDate;
    }

    // Calculate the monthly cost
    public double getMonthlyCost() {
        if (billingCycle == BillingCycle.YEARLY) {
            return cost / 12;
        }

        return cost;
    }

    // Display subscription details
    public String toString() {
        return id + " | " + name + " | " + cost + " | "
                + billingCycle + " | last used: " + lastUsedDate;
    }
}

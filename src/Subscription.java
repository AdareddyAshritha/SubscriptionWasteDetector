import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

// Stores the details of one subscription.
public class Subscription {
    private int id;
    private String name;
    private double cost;
    private BillingCycle billingCycle;
    private LocalDate lastUsedDate;

    // Initializes the subscription details.
    public Subscription(int id, String name, double cost,
                        BillingCycle billingCycle, LocalDate lastUsedDate) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.billingCycle = billingCycle;
        this.lastUsedDate = lastUsedDate;
    }

    // Returns the subscription ID.
    public int getId() {
        return id;
    }

    // Returns the subscription name.
    public String getName() {
        return name;
    }

    // Returns the subscription cost.
    public double getCost() {
        return cost;
    }

    // Returns the billing cycle.
    public BillingCycle getBillingCycle() {
        return billingCycle;
    }

    // Returns the last-used date.
    public LocalDate getLastUsedDate() {
        return lastUsedDate;
    }

    // Updates the subscription name.
    public void setName(String name) {
        this.name = name;
    }

    // Updates the subscription cost.
    public void setCost(double cost) {
        this.cost = cost;
    }

    // Updates the billing cycle.
    public void setBillingCycle(BillingCycle billingCycle) {
        this.billingCycle = billingCycle;
    }

    // Updates the last-used date.
    public void setLastUsedDate(LocalDate lastUsedDate) {
        this.lastUsedDate = lastUsedDate;
    }

    // Converts yearly cost into a monthly cost.
    public double getMonthlyCost() {
        if (billingCycle == BillingCycle.YEARLY) {
            return cost / 12.0;
        }

        return cost;
    }

    // Calculates how many days have passed since last use.
    public long getDaysSinceLastUse() {
        return ChronoUnit.DAYS.between(lastUsedDate, LocalDate.now());
    }

    // Checks whether the subscription has been unused for enough days.
    public boolean isUnused(int thresholdDays) {
        return getDaysSinceLastUse() >= thresholdDays;
    }

    // Displays subscription information in readable form.
    @Override
    public String toString() {
        return "ID: " + id
                + " | Name: " + name
                + " | Cost: " + String.format("%.2f", cost)
                + " | Cycle: " + billingCycle
                + " | Last used: " + lastUsedDate;
    }
}
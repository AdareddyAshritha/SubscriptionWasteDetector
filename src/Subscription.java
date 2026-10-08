import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
    //ChroUnit.DAYS.between() calculates the gap between two dates.
    public long getDaysSinceLastUse(){
        return ChronoUnit.DAYS.between(lastUsedDate, LocalDate.now());
    }
    //If more days have passed than the given limit, it is considered unused.
    public boolean isUnused(int thresholdDays){
        return getDaysSinceLastUse() > thresholdDays;
    }
    // Display subscription details
    public String toString() {
        return id + " | " + name + " | " + cost + " | "
                + billingCycle + " | last used: " + lastUsedDate;
    }
}

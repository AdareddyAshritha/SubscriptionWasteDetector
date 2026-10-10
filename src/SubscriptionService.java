import java.time.LocalDate;
import java.util.ArrayList;

// Performs the main subscription operations.
public class SubscriptionService {
    private ArrayList<Subscription> subscriptions;
    private int nextId = 1;

    // Loads existing subscriptions when the service starts.
    public SubscriptionService() {
        subscriptions = FileHandler.load();

        // Finds the next available ID.
        for (Subscription s : subscriptions) {
            if (s.getId() >= nextId) {
                nextId = s.getId() + 1;
            }
        }
    }

    // Adds a new subscription and saves the updated list.
    public void addSubscription(String name, double cost,
                                BillingCycle cycle, LocalDate lastUsed) {
        Subscription s = new Subscription(
                nextId, name, cost, cycle, lastUsed);

        subscriptions.add(s);
        nextId++;

        FileHandler.save(subscriptions);
    }

    // Returns all subscriptions.
    public ArrayList<Subscription> getAll() {
        return subscriptions;
    }

    // Calculates the total monthly expense.
    public double getTotalMonthlyCost() {
        double total = 0;

        for (Subscription s : subscriptions) {
            total += s.getMonthlyCost();
        }

        return total;
    }

    // Returns subscriptions unused for the given number of days.
    public ArrayList<Subscription> getUnusedSubscriptions(int days) {
        ArrayList<Subscription> unused = new ArrayList<>();

        for (Subscription s : subscriptions) {
            if (s.isUnused(days)) {
                unused.add(s);
            }
        }

        return unused;
    }
}

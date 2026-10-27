import java.util.ArrayList;

public class SubscriptionService {

    private ArrayList<Subscription> subscriptions;
    private int nextId;

    // Load existing subscriptions when the program starts
    public SubscriptionService() {
        subscriptions = FileHandler.load();
        nextId = 1;

        // Find the next available ID
        for (Subscription s : subscriptions) {
            if (s.getId() >= nextId) {
                nextId = s.getId() + 1;
            }
        }
    }

    // Add a new subscription
    public void addSubscription(Subscription s) {
        // Assign a unique ID
        s = new Subscription(
                nextId,
                s.getName(),
                s.getCost(),
                s.getBillingCycle(),
                s.getLastUsedDate()
        );

        subscriptions.add(s);
        nextId++;

        // Save subscriptions to the file
        FileHandler.save(subscriptions);
    }

    // Return all subscriptions
    public ArrayList<Subscription> getAllSubscriptions() {
        return subscriptions;
    }

    // Calculate total monthly cost
    public double getTotalMonthlyCost() {
        double total = 0;

        for (Subscription s : subscriptions) {
            total += s.getMonthlyCost();
        }

        return total;
    }

    // Find subscriptions unused for a specified number of days
    public ArrayList<Subscription> getUnusedSubscriptions(int days) {
        ArrayList<Subscription> unused = new ArrayList<>();

        for (Subscription s : subscriptions) {
            if (s.isUnused(days)) {
                unused.add(s);
            }
        }

        return unused;
    }

    // Search subscriptions by name
    public ArrayList<Subscription> searchSubscriptions(String keyword) {
        ArrayList<Subscription> results = new ArrayList<>();

        for (Subscription s : subscriptions) {
            if (s.getName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                results.add(s);
            }
        }

        return results;
    }

    // Update a subscription
    public boolean updateSubscription(Subscription updated) {
        for (int i = 0; i < subscriptions.size(); i++) {
            if (subscriptions.get(i).getId() == updated.getId()) {
                subscriptions.set(i, updated);
                FileHandler.save(subscriptions);
                return true;
            }
        }

        return false;
    }

    // Delete a subscription using its ID
    public boolean deleteSubscription(int id) {
        boolean found = false;

        for (int i = 0; i < subscriptions.size(); i++) {
            if (subscriptions.get(i).getId() == id) {
                subscriptions.remove(i);
                found = true;
                break;
            }
        }

        // Save only if a subscription was deleted
        if (found) {
            FileHandler.save(subscriptions);
        }

        return found;
    }
}
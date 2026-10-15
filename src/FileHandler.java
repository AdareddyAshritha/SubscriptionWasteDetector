import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

// Handles saving and loading subscription data.
public class FileHandler {

    private static final String FILE_NAME = "subscriptions.txt";

    // Saves all subscriptions to a text file.
    public static void save(ArrayList<Subscription> list) {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {

            for (Subscription s : list) {
                writer.write(
                        s.getId() + ","
                                + s.getName().replace(",", " ") + ","
                                + s.getCost() + ","
                                + s.getBillingCycle() + ","
                                + s.getLastUsedDate() + "\n"
                );
            }

        } catch (IOException e) {
            System.out.println("Could not save subscription data.");
        }
    }

    // Loads subscriptions from the text file.
    public static ArrayList<Subscription> load() {
        ArrayList<Subscription> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Returns an empty list if the file does not exist.
        if (!file.exists()) {
            return list;
        }

        try (Scanner reader = new Scanner(file)) {

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                // Each record must contain five fields.
                if (parts.length != 5) {
                    System.out.println(
                            "Skipping invalid saved record.");
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double cost = Double.parseDouble(parts[2]);
                    BillingCycle cycle =
                            BillingCycle.valueOf(parts[3]);
                    LocalDate lastUsed =
                            LocalDate.parse(parts[4]);

                    // Reject invalid saved values.
                    if (id <= 0 || name.trim().isEmpty()
                            || !Double.isFinite(cost) || cost < 0
                            || lastUsed.isAfter(LocalDate.now())) {
                        System.out.println(
                                "Skipping invalid saved record.");
                        continue;
                    }

                    list.add(new Subscription(
                            id, name, cost, cycle, lastUsed));

                } catch (IllegalArgumentException e) {
                    System.out.println(
                            "Skipping invalid saved record.");
                }
            }

        } catch (IOException e) {
            System.out.println("Could not read subscription data.");
        }

        return list;
    }
}
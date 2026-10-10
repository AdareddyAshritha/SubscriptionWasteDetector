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
                writer.write(s.getId() + ","
                        + s.getName().replace(",", " ") + ","
                        + s.getCost() + ","
                        + s.getBillingCycle() + ","
                        + s.getLastUsedDate() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not save subscription data.");
        }
    }

    // Loads saved subscriptions from the text file.
    public static ArrayList<Subscription> load() {
        ArrayList<Subscription> list = new ArrayList<>();
        File file = new File(FILE_NAME);

        // Returns an empty list if no saved file exists.
        if (!file.exists()) {
            return list;
        }

        // Reads the file and recreates subscription objects.
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);

                // Each record must contain five fields.
                if (parts.length != 5) {
                    System.out.println("Skipping invalid saved record.");
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double cost = Double.parseDouble(parts[2]);
                    BillingCycle cycle = BillingCycle.valueOf(parts[3]);
                    LocalDate lastUsed = LocalDate.parse(parts[4]);

                    // Creates the object using the saved details.
                    list.add(new Subscription(
                            id, name, cost, cycle, lastUsed));

                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid saved record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read subscription data.");
        }

        return list;
    }
}

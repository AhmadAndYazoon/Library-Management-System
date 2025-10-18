package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FineStorage {
    private static final String FINE_FILE = "fines.txt";

    public static List<Fine> loadFines() {
        List<Fine> fines = new ArrayList<>();
        File file = new File(FINE_FILE);
        if (!file.exists()) return fines;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                fines.add(new Fine(parts[0], Double.parseDouble(parts[1])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fines;
    }

    public static void saveFines(List<Fine> fines) {
        try (FileWriter writer = new FileWriter(FINE_FILE)) {
            for (Fine f : fines) {
                writer.write(f.email + "," + f.amount + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getFineAmount(String email) {
        return loadFines().stream()
                .filter(f -> f.email.equalsIgnoreCase(email))
                .map(f -> f.amount)
                .findFirst()
                .orElse(0.0);
    }

    public static void updateFine(String email, double newAmount) {
        List<Fine> fines = loadFines();
        boolean found = false;
        for (Fine f : fines) {
            if (f.email.equalsIgnoreCase(email)) {
                f.amount = newAmount;
                found = true;
                break;
            }
        }
        if (!found && newAmount > 0) {
            fines.add(new Fine(email, newAmount));
        }
        saveFines(fines);
    }
}

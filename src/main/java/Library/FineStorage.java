package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for persisting and querying {@link Fine} records
 * from a simple text file (fines.txt).
 */
public class FineStorage {

    /** Path of the fines data file. */
	private static final String FINE_FILE =
	        System.getProperty("fines.file", "fines.txt");

    /**
     * Loads all fines from the storage file.
     *
     * @return list of fines; empty list if file does not exist
     */
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

    /**
     * Saves all given fines into the storage file, overwriting any existing data.
     *
     * @param fines list of fines to save
     */
    public static void saveFines(List<Fine> fines) {
        try (FileWriter writer = new FileWriter(FINE_FILE)) {
            for (Fine f : fines) {
                writer.write(f.email + "," + f.amount + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the current fine amount for a user with the given email.
     *
     * @param email user email
     * @return fine amount; 0 if the user has no fine record
     */
    public static double getFineAmount(String email) {
        return loadFines().stream()
                .filter(f -> f.email.equalsIgnoreCase(email))
                .map(f -> f.amount)
                .findFirst()
                .orElse(0.0);
    }

    /**
     * Updates the fine amount for the given email.
     * If the user does not have a fine yet and {@code newAmount > 0},
     * a new record is created.
     *
     * @param email     user email
     * @param newAmount new fine amount
     */
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

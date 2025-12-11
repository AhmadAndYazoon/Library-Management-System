package Library;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for persisting and loading {@link Borrow} records
 * from a simple text file (borrows.txt).
 */
public class BorrowStorage {

    /** Path of the borrows data file. */
	private static final String FILE =
	        System.getProperty("borrows.file", "borrows.txt");
    /**
     * Saves all given borrow records into the storage file, overwriting any existing data.
     *
     * @param borrows list of borrows to save
     */
    public static void saveBorrowed(List<Borrow> borrows) {
        try (FileWriter w = new FileWriter(FILE)) {
            for (Borrow b : borrows) {
                w.write(b.title + "," +
                        b.author + "," +
                        b.isbn + "," +
                        b.username + "," +
                        b.email + "," +
                        b.dueDate + "," +
                        b.mediaType + "\n");
            }
        } catch (Exception e) {
        }
    }

    /**
     * Loads all borrow records from the storage file.
     *
     * @return list of borrows; empty list if file cannot be read
     */
    public static List<Borrow> loadBorrowed() {
        List<Borrow> list = new ArrayList<>();
        try (BufferedReader r = new BufferedReader(new FileReader(FILE))) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] p = line.split(",");
                list.add(new Borrow(
                        p[0],
                        p[1],
                        p[2],
                        p[3],
                        p[4],
                        LocalDate.parse(p[5]),
                        p[6]
                ));
            }
        } catch (Exception e) {
        }
        return list;
    }

    /**
     * Appends a new borrow record to the existing list and saves it.
     *
     * @param b borrow to add
     */
    public static void addBorrow(Borrow b) {
        List<Borrow> all = loadBorrowed();
        all.add(b);
        saveBorrowed(all);
    }

    /**
     * Removes all borrow records that match the given ISBN.
     *
     * @param isbn identifier of the item to remove borrows for
     */
    public static void removeByISBN(String isbn) {
        List<Borrow> all = loadBorrowed();
        all.removeIf(b -> b.isbn.equals(isbn));
        saveBorrowed(all);
    }
}

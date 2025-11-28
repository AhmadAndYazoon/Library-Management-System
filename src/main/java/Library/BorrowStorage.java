package Library;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BorrowStorage {
    private static final String FILE = "borrows.txt";

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
        } catch (Exception e) {}
    }

    public static void addBorrow(Borrow b) {
        List<Borrow> all = loadBorrowed();
        all.add(b);
        saveBorrowed(all);
    }

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
        } catch (Exception e) {}
        return list;
    }


    public static void removeByISBN(String isbn) {
        List<Borrow> all = loadBorrowed();
        all.removeIf(b -> b.isbn.equals(isbn));
        saveBorrowed(all);
    }
}

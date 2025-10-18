package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
public class BorrowStorage {
    private static final String BOOKS_FILE = "borrowed.txt";

    
    public static void saveBorrowed(List<Borrow> books) {
        try (FileWriter writer = new FileWriter(new File(BOOKS_FILE))) {
            for(Borrow b:books) {
                String line = b.title+","+b.author+","+b.isbn+","+b.username+","+b.email+","+b.dueDate;
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @SuppressWarnings("unchecked")
    public static List<Borrow> loadBorrowed() {
        List<Borrow> borrows = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while((line = reader.readLine()) != null) {
                String parts[] = line.split(",");
                borrows.add(new Borrow(parts[0],parts[1],parts[2],parts[3],parts[4],LocalDate.parse(parts[5])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return borrows;
    }
    public static void addBorrow(Borrow record) {
        List<Borrow> all = loadBorrowed();
        all.add(record);
        saveBorrowed(all);
    }

    public static void removeByISBN(String isbn) {
        List<Borrow> all = loadBorrowed();
        all.removeIf(b -> b.isbn.equals(isbn));
        saveBorrowed(all);
    }
}

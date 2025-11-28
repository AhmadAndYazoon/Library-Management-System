package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookStorage {
    private static final String BOOKS_FILE = "books.txt";

    
    public static void saveBooks(List<Book> books) {
        try (FileWriter writer = new FileWriter(new File(BOOKS_FILE))) {
            for(Book b:books) {
                String str = (b.isBorrowed)?"yes":"no";
                String line = b.title+","+b.author+","+b.ISBN+","+str;
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    @SuppressWarnings("unchecked")
    public static List<Book> loadBooks() {
        List<Book> Books = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while((line = reader.readLine()) != null) {
                String parts[] = line.split(",");
                boolean b = (parts[3].equals("yes"))? true : false;
                Books.add(new Book(parts[0],parts[1],parts[2],b));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return Books;
    }
}

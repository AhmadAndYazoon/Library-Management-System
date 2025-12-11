package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for persisting and loading {@link Book} objects
 * from a simple text file (books.txt).
 */
public class BookStorage {
	private BookStorage() {
	    throw new UnsupportedOperationException("Utility class");
	}

    /** Path of the books data file. */
	private static final String BOOKS_FILE =
	        System.getProperty("books.file", "books.txt");
    /**
     * Saves all given books into the storage file, overwriting any existing data.
     *
     * @param books list of books to save
     */
    public static void saveBooks(List<Book> books) {
        try (FileWriter writer = new FileWriter(new File(BOOKS_FILE))) {
            for (Book b : books) {
                String str = (b.isBorrowed) ? "yes" : "no";
                String line = b.title + "," + b.author + "," + b.ISBN + "," + str;
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all books from the storage file.
     *
     * @return list of books; empty list if file cannot be read
     */
    public static List<Book> loadBooks() {
        List<Book> Books = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(BOOKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String parts[] = line.split(",");
                boolean b = (parts[3].equals("yes")) ? true : false;
                Books.add(new Book(parts[0], parts[1], parts[2], b));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return Books;
    }
}


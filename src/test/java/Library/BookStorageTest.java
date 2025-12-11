package Library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookStorageTest {

    private static final String TEST_FILE = "src/test/resources/books_test.txt";
    private static final Path booksFilePath = Paths.get(TEST_FILE);

    @BeforeAll
    static void useTestFile() {
        System.setProperty("books.file", TEST_FILE);
    }

    private void clearFile() throws IOException {
        Files.writeString(booksFilePath, "");
    }

    @Test
    void testSaveAndLoadBooks() throws IOException {
        clearFile();

        Book b1 = new Book("Title1", "Author1", "111", false);
        Book b2 = new Book("Title2", "Author2", "222", true);

        List<Book> books = List.of(b1, b2);

        BookStorage.saveBooks(books);

        assertTrue(Files.exists(booksFilePath));

        List<Book> loaded = BookStorage.loadBooks();

        assertEquals(2, loaded.size());

        Book lb1 = loaded.get(0);
        assertEquals("Title1", lb1.title);
        assertEquals("Author1", lb1.author);
        assertEquals("111", lb1.ISBN);
        assertFalse(lb1.isBorrowed);

        Book lb2 = loaded.get(1);
        assertEquals("Title2", lb2.title);
        assertEquals("Author2", lb2.author);
        assertEquals("222", lb2.ISBN);
        assertTrue(lb2.isBorrowed);
    }

    @Test
    void testLoadBooksWhenFileEmpty() throws IOException {
        clearFile();

        List<Book> result = BookStorage.loadBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}


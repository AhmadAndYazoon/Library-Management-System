package Library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageTest {

    @BeforeEach
    void resetFile() {
        FileStorage.saveBooks(new ArrayList<>());
    }

    @Test
    void saveAndLoadBooks_shouldPersistDataCorrectly() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("Title1", "Author1", "ISBN1", false));
        list.add(new Book("Title2", "Author2", "ISBN2", true));

        FileStorage.saveBooks(list);

        List<Book> loaded = FileStorage.loadBooks();

        assertEquals(2, loaded.size(), "Should load 2 books");

        Book b1 = loaded.get(0);
        assertEquals("Title1", b1.title);
        assertEquals("Author1", b1.author);
        assertEquals("ISBN1", b1.ISBN);
        assertFalse(b1.isBorrowed);

        Book b2 = loaded.get(1);
        assertEquals("Title2", b2.title);
        assertEquals("Author2", b2.author);
        assertEquals("ISBN2", b2.ISBN);
        assertTrue(b2.isBorrowed);
    }

    @Test
    void saveBooks_shouldWriteBorrowedAsYesOrNo() {
        List<Book> list = new ArrayList<>();
        list.add(new Book("T1", "A1", "I1", true));
        list.add(new Book("T2", "A2", "I2", false));

        FileStorage.saveBooks(list);

        List<Book> loaded = FileStorage.loadBooks();

        assertTrue(loaded.get(0).isBorrowed, "First book should be borrowed (yes)");
        assertFalse(loaded.get(1).isBorrowed, "Second book should NOT be borrowed (no)");
    }

    @Test
    void loadBooks_shouldReturnEmptyListIfFileMissingOrCorrupt() {
        List<Book> loaded = FileStorage.loadBooks();

        assertNotNull(loaded, "Loaded list should not be null");
        assertTrue(loaded.isEmpty(), "Loaded list should be empty for empty file");
    }
    @Test
    void loadBooks_shouldReturnEmptyListIfFileMissing() {
        File f = new File("books.txt");
        if (f.exists()) {
            assertTrue(f.delete(), "books.txt should be deletable for this test");
        }

        List<Book> loaded = FileStorage.loadBooks();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty(), "List should be empty when books file is missing");
    }

}

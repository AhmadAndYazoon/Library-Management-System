package Library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BorrowStorageTest {

    @BeforeEach
    void resetFile() {
        BorrowStorage.saveBorrowed(new ArrayList<>());
    }

    @Test
    void saveAndLoadBorrowed_shouldPersistDataCorrectly() {
        List<Borrow> list = new ArrayList<>();
        list.add(new Borrow("Title1", "Author1", "ISBN1", "user1", "u1@test.com", LocalDate.of(2025, 1, 1)));
        list.add(new Borrow("Title2", "Author2", "ISBN2", "user2", "u2@test.com", LocalDate.of(2025, 2, 2)));

        BorrowStorage.saveBorrowed(list);

        List<Borrow> loaded = BorrowStorage.loadBorrowed();

        assertEquals(2, loaded.size(), "Should load 2 records");

        Borrow b1 = loaded.get(0);
        assertEquals("Title1", b1.title);
        assertEquals("Author1", b1.author);
        assertEquals("ISBN1", b1.isbn);
        assertEquals("user1", b1.username);
        assertEquals("u1@test.com", b1.email);
        assertEquals(LocalDate.of(2025, 1, 1), b1.dueDate);

        Borrow b2 = loaded.get(1);
        assertEquals("Title2", b2.title);
        assertEquals("Author2", b2.author);
        assertEquals("ISBN2", b2.isbn);
        assertEquals("user2", b2.username);
        assertEquals("u2@test.com", b2.email);
        assertEquals(LocalDate.of(2025, 2, 2), b2.dueDate);
    }

    @Test
    void addBorrow_shouldAddNewRecordToFile() {

        Borrow b1 = new Borrow("T1", "A1", "I1", "u1", "e1@test.com", LocalDate.now());
        BorrowStorage.addBorrow(b1);

        List<Borrow> loaded = BorrowStorage.loadBorrowed();
        assertEquals(1, loaded.size(), "Should have 1 record after first add");
        assertEquals("I1", loaded.get(0).isbn);

        Borrow b2 = new Borrow("T2", "A2", "I2", "u2", "e2@test.com", LocalDate.now().plusDays(1));
        BorrowStorage.addBorrow(b2);

        loaded = BorrowStorage.loadBorrowed();
        assertEquals(2, loaded.size(), "Should have 2 records after second add");
        assertEquals("I1", loaded.get(0).isbn);
        assertEquals("I2", loaded.get(1).isbn);
    }

    @Test
    void removeByISBN_shouldRemoveCorrectRecord() {
        List<Borrow> list = new ArrayList<>();
        list.add(new Borrow("Title1", "Author1", "I1", "u1", "e1@test.com", LocalDate.now()));
        list.add(new Borrow("Title2", "Author2", "I2", "u2", "e2@test.com", LocalDate.now()));
        list.add(new Borrow("Title3", "Author3", "I3", "u3", "e3@test.com", LocalDate.now()));

        BorrowStorage.saveBorrowed(list);

        BorrowStorage.removeByISBN("I2");

        List<Borrow> loaded = BorrowStorage.loadBorrowed();
        assertEquals(2, loaded.size(), "Should have 2 records after removal");

        boolean hasI2 = loaded.stream().anyMatch(b -> b.isbn.equals("I2"));
        assertFalse(hasI2, "Record with ISBN I2 should be removed");

        boolean hasI1 = loaded.stream().anyMatch(b -> b.isbn.equals("I1"));
        boolean hasI3 = loaded.stream().anyMatch(b -> b.isbn.equals("I3"));
        assertTrue(hasI1, "Record with ISBN I1 should remain");
        assertTrue(hasI3, "Record with ISBN I3 should remain");
    }
    
    @Test
    void loadBorrowed_shouldReturnEmptyListIfFileIsMissing() {
        File f = new File("borrowed.txt");
        if (f.exists()) {
            assertTrue(f.delete(), "borrowed.txt should be deletable for this test");
        }

        List<Borrow> loaded = BorrowStorage.loadBorrowed();

        assertNotNull(loaded, "List must not be null even if file is missing");
        assertTrue(loaded.isEmpty(), "List should be empty when file is missing");
    }

}

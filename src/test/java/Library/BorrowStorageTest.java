package Library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BorrowStorageTest {

    private static final String TEST_FILE = "src/test/resources/borrows_test.txt";
    private static final Path filePath = Paths.get(TEST_FILE);
    private static final String TITLE1 = "Title1";
    private static final String AUTHOR1 = "Author1";
   
    private static final String UTEST = "u2@test.com";
    private static final String USERTWO = "User Two";
    private static final String USERONE = "User One";

    @BeforeAll
    static void useTestFile() {
        System.setProperty("borrows.file", TEST_FILE);
    }

    private void clearFile() throws IOException {
        Files.writeString(filePath, "");
    }

    @Test
    void testSaveAndLoadBorrowed() throws IOException {
        clearFile();

        Borrow b1 = new Borrow(
        		TITLE1,
        		AUTHOR1,
                "111",
                USERONE,
                "u1@test.com",
                LocalDate.now().plusDays(3),
                "BOOK"
        );

        Borrow b2 = new Borrow(
        		TITLE1,
                "Author2",
                "222",
                USERTWO,
                UTEST,
                LocalDate.now().minusDays(2),
                "CD"
        );

        List<Borrow> borrows = List.of(b1, b2);

        BorrowStorage.saveBorrowed(borrows);

        assertTrue(Files.exists(filePath));

        List<Borrow> loaded = BorrowStorage.loadBorrowed();

        assertEquals(2, loaded.size());
    }

    @Test
    void testAddBorrow() throws IOException {
        clearFile();

        Borrow b1 = new Borrow(
        		TITLE1,
        		AUTHOR1,
                "111",
                USERONE,
                "u1@test.com",
                LocalDate.now(),
                "BOOK"
        );
        Borrow b2 = new Borrow(
        		TITLE1,
                "Author2",
                "222",
                USERTWO,
                UTEST,
                LocalDate.now().plusDays(1),
                "CD"
        );

        BorrowStorage.addBorrow(b1);
        BorrowStorage.addBorrow(b2);

        List<Borrow> loaded = BorrowStorage.loadBorrowed();

        assertEquals(2, loaded.size());
    }

    @Test
    void testRemoveByISBN() throws IOException {
        clearFile();

        Borrow b1 = new Borrow(
        		TITLE1,
        		AUTHOR1,
                "111",
                USERONE,
                "u1@test.com",
                LocalDate.now(),
                "BOOK"
        );
        Borrow b2 = new Borrow(
                "Title2",
                "Author2",
                "111",
                USERTWO,
                UTEST,
                LocalDate.now().plusDays(1),
                "BOOK"
        );
        Borrow b3 = new Borrow(
                "Title3",
                "Author3",
                "333",
                "User Three",
                "u3@test.com",
                LocalDate.now().plusDays(2),
                "CD"
        );

        BorrowStorage.saveBorrowed(List.of(b1, b2, b3));

        BorrowStorage.removeByISBN("111");

        List<Borrow> loaded = BorrowStorage.loadBorrowed();

        assertEquals(1, loaded.size());
        assertEquals("333", loaded.get(0).isbn);
        assertEquals("Title3", loaded.get(0).title);
    }

    @Test
    void testLoadBorrowedWhenFileEmpty() throws IOException {
        clearFile();

        List<Borrow> list = BorrowStorage.loadBorrowed();

        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
}


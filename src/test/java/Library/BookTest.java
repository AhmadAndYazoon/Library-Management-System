package Library;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class BookTest {

    @Test
    void testDefaultConstructor() {
        Book b = new Book();
        assertFalse(b.isBorrowed);
        assertNull(b.dueDate);
    }

    @Test
    void testParameterizedConstructor() {
        Book b = new Book("Title", "Author", "123", false);
        assertEquals("Title", b.title);
        assertEquals("Author", b.author);
        assertEquals("123", b.ISBN);
        assertFalse(b.isBorrowed);
        assertNull(b.dueDate);
    }

    @Test
    void testBorrowBook() {
        Book b = new Book();
        b.borrowBook();

        assertTrue(b.isBorrowed);
        assertNotNull(b.dueDate);
        assertEquals(LocalDate.now().plusDays(28), b.dueDate);
    }

    @Test
    void testReturnBook() {
        Book b = new Book();
        b.borrowBook();
        b.returnBook();

        assertFalse(b.isBorrowed);
        assertNull(b.dueDate);
    }
}

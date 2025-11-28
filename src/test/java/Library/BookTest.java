package Library;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void defaultConstructor_shouldInitializeBorrowedFalse() {
        Book book = new Book();

        assertFalse(book.isBorrowed, "Book should not be borrowed by default");
        assertNull(book.dueDate, "Default due date should be null");
    }

    @Test
    void parameterizedConstructor_shouldStoreValuesCorrectly() {
        Book book = new Book("Clean Code", "Robert Martin", "12345", false);

        assertEquals("Clean Code", book.title);
        assertEquals("Robert Martin", book.author);
        assertEquals("12345", book.ISBN);
        assertFalse(book.isBorrowed);
        assertNull(book.dueDate);
    }

    @Test
    void borrowBook_shouldSetBorrowedTrueAndAssignDueDate() {
        Book book = new Book("Java", "Tayseer", "9999", false);

        book.borrowBook();

        assertTrue(book.isBorrowed, "Book should be marked as borrowed");
        assertNotNull(book.dueDate, "Due date should be assigned after borrowing");


        assertEquals(LocalDate.now().plusDays(28), book.dueDate);
    }

    @Test
    void returnBook_shouldSetBorrowedFalseAndClearDueDate() {
        Book book = new Book("Anything", "Someone", "0000", true);

    
        book.borrowBook();


        book.returnBook();

        assertFalse(book.isBorrowed, "Book should be marked as not borrowed");
        assertNull(book.dueDate, "Due date should be cleared after returning");
    }
}

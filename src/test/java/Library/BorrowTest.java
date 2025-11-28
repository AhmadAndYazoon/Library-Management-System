package Library;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BorrowTest {

    @Test
    void constructor_shouldStoreValuesCorrectly() {
        LocalDate date = LocalDate.of(2025, 1, 10);

        Borrow b = new Borrow(
                "Title Example",
                "Author Example",
                "12345",
                "taysir",
                "email@test.com",
                date
        );

        assertEquals("Title Example", b.title);
        assertEquals("Author Example", b.author);
        assertEquals("12345", b.isbn);
        assertEquals("taysir", b.username);
        assertEquals("email@test.com", b.email);
        assertEquals(date, b.dueDate);
    }

    @Test
    void isOverDueBorrow_shouldReturnTrue_WhenDateBeforeToday() {
        LocalDate oldDate = LocalDate.now().minusDays(5);
        Borrow b = new Borrow("t","a","1","u","e", oldDate);

        assertTrue(b.isOverDueBorrow());
    }

    @Test
    void isOverDueBorrow_shouldReturnFalse_WhenDateAfterToday() {
        LocalDate futureDate = LocalDate.now().plusDays(3);
        Borrow b = new Borrow("t","a","1","u","e", futureDate);

        assertFalse(b.isOverDueBorrow());
    }

    @Test
    void isOverDueBorrow_shouldReturnFalse_WhenDateIsToday() {
        LocalDate today = LocalDate.now();
        Borrow b = new Borrow("t","a","1","u","e", today);

        assertFalse(b.isOverDueBorrow());
    }
}

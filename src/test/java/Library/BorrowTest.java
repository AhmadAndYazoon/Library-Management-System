package Library;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class BorrowTest {

    @Test
    void testIsOverdue_True() {
        Borrow b = new Borrow(
                "T1", "A1", "111",
                "User", "u@test.com",
                LocalDate.now().minusDays(1),
                "BOOK"
        );
        assertTrue(b.isOverDueBorrow());
    }

    @Test
    void testIsOverdue_False_Future() {
        Borrow b = new Borrow(
                "T1", "A1", "111",
                "User", "u@test.com",
                LocalDate.now().plusDays(3),
                "BOOK"
        );
        assertFalse(b.isOverDueBorrow());
    }

    @Test
    void testIsOverdue_False_Today() {
        Borrow b = new Borrow(
                "T1", "A1", "111",
                "User", "u@test.com",
                LocalDate.now(),
                "BOOK"
        );
        assertFalse(b.isOverDueBorrow());
    }
    
    
    
}

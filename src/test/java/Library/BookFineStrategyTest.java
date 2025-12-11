package Library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class BookFineStrategyTest {

    @Test
    void testCalculateFine() {
        BookFineStrategy strategy = new BookFineStrategy();

        assertEquals(0, strategy.calculateFine(0));
        assertEquals(10, strategy.calculateFine(1));
        assertEquals(50, strategy.calculateFine(5));
        assertEquals(100, strategy.calculateFine(10));
    }
}

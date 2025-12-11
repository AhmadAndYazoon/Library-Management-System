package Library;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CDFineStrategyTest {

    @Test
    void testCalculateFine() {
        CDFineStrategy strategy = new CDFineStrategy();

        assertEquals(0, strategy.calculateFine(0));
        assertEquals(20, strategy.calculateFine(1));
        assertEquals(100, strategy.calculateFine(5));
        assertEquals(200, strategy.calculateFine(10));
    }
}

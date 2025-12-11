package Library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FineTest {

    @Test
    void testFineConstructorAndFields() {
        Fine fine = new Fine("test@gmail.com", 50.0);

        assertEquals("test@gmail.com", fine.email);
        assertEquals(50.0, fine.amount);
    }
}

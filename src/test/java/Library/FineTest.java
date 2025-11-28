package Library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FineTest {

    @Test
    void constructor_shouldStoreValuesCorrectly() {
        Fine fine = new Fine("test@example.com", 25.5);

        assertEquals("test@example.com", fine.email);
        assertEquals(25.5, fine.amount);
    }
}

package Library;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class CDTest {

    @Test
    void testDefaultConstructor() {
        CD cd = new CD();
        assertFalse(cd.isBorrowed);
        assertNull(cd.dueDate);
    }

    @Test
    void testParameterizedConstructor() {
        CD cd = new CD("Title", "Artist", "999", false);
        assertEquals("Title", cd.title);
        assertEquals("Artist", cd.author);
        assertEquals("999", cd.ISBN);
        assertFalse(cd.isBorrowed);
    }

    @Test
    void testBorrowCD() {
        CD cd = new CD();
        cd.borrowBook();

        assertTrue(cd.isBorrowed);
        assertNotNull(cd.dueDate);
        assertEquals(LocalDate.now().plusDays(7), cd.dueDate);
    }
}

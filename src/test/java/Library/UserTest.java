package Library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_shouldStoreValuesCorrectly() {
        User user = new User("Tayseer", "t@test.com", "1234", "admin");

        assertEquals("Tayseer", user.fullName);
        assertEquals("t@test.com", user.email);
        assertEquals("1234", user.password);
        assertEquals("admin", user.role);
    }
}

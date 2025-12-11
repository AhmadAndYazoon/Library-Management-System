package Library;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserConstructorAndFields() {

        User user = new User("Tareq", "t@test.com", "12345", "admin");

        assertEquals("Tareq", user.fullName);
        assertEquals("t@test.com", user.email);
        assertEquals("12345", user.password);
        assertEquals("admin", user.role);
    }
}

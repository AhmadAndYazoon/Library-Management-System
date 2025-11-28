package Library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

    @BeforeEach
    void resetFile() {
        UserStorage.saveUsers(new ArrayList<>());
    }

    @Test
    void saveAndLoadUsers_shouldPersistDataCorrectly() {
        List<User> users = new ArrayList<>();
        users.add(new User("Tayseer", "t@test.com", "1234", "user"));
        users.add(new User("Ahmad", "a@test.com", "pass", "admin"));

        UserStorage.saveUsers(users);

        List<User> loaded = UserStorage.loadUsers();

        assertEquals(2, loaded.size());

        User u1 = loaded.get(0);
        assertEquals("Tayseer", u1.fullName);
        assertEquals("t@test.com", u1.email);
        assertEquals("1234", u1.password);
        assertEquals("user", u1.role);

        User u2 = loaded.get(1);
        assertEquals("Ahmad", u2.fullName);
        assertEquals("a@test.com", u2.email);
        assertEquals("pass", u2.password);
        assertEquals("admin", u2.role);
    }

    @Test
    void addUser_shouldAppendUserToFile() {
        UserStorage.addUser(new User("A", "a@a.com", "1", "user"));
        UserStorage.addUser(new User("B", "b@b.com", "2", "user"));

        List<User> loaded = UserStorage.loadUsers();

        assertEquals(2, loaded.size());
        assertEquals("A", loaded.get(0).fullName);
        assertEquals("B", loaded.get(1).fullName);
    }

    @Test
    void findUserByEmail_shouldReturnCorrectUser() {
        UserStorage.addUser(new User("A", "a@a.com", "1", "user"));
        UserStorage.addUser(new User("B", "b@b.com", "2", "admin"));

        User found = UserStorage.findUserByEmail("b@b.com");

        assertNotNull(found);
        assertEquals("B", found.fullName);
        assertEquals("admin", found.role);
    }

    @Test
    void findUserByEmail_shouldReturnNullIfNotFound() {
        UserStorage.addUser(new User("A", "a@a.com", "1", "user"));

        User found = UserStorage.findUserByEmail("notfound@test.com");

        assertNull(found);
    }
    @Test
    void loadUsers_shouldReturnEmptyListIfFileMissing() {
        File f = new File("users.txt");
        if (f.exists()) {
            assertTrue(f.delete(), "users.txt should be deletable for this test");
        }

        List<User> loaded = UserStorage.loadUsers();

        assertNotNull(loaded);
        assertTrue(loaded.isEmpty(), "List should be empty when users file is missing");
    }

}

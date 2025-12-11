package Library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserStorageTest {

	 private static final String TEST_FILE = "src/test/resources/users_test.txt";
	    private static final Path filePath = Paths.get(TEST_FILE);

	    @BeforeAll
	    static void useTestFile() {
	        System.setProperty("users.file", TEST_FILE);
	    }

	    private void clearFile() throws IOException {
	    	Files.deleteIfExists(filePath); // يفضي ملف التست
	    }

    @Test
    void testSaveAndLoadUsers() throws IOException {

        clearFile();

        User u1 = new User("Tareq", "t@test.com", "123", "admin");
        User u2 = new User("Maya", "m@test.com", "456", "user");

        List<User> users = List.of(u1, u2);

        UserStorage.saveUsers(users);

        assertTrue(Files.exists(filePath));

        List<User> loaded = UserStorage.loadUsers();

        assertEquals(2, loaded.size());

        User lu1 = loaded.get(0);
        assertEquals("Tareq", lu1.fullName);
        assertEquals("t@test.com", lu1.email);
        assertEquals("123", lu1.password);
        assertEquals("admin", lu1.role);

        User lu2 = loaded.get(1);
        assertEquals("Maya", lu2.fullName);
        assertEquals("m@test.com", lu2.email);
        assertEquals("456", lu2.password);
        assertEquals("user", lu2.role);
    }

    @Test
    void testAddUser() throws IOException {

        clearFile();

        User u1 = new User("Tareq", "t@test.com", "123", "admin");
        User u2 = new User("Maya", "m@test.com", "456", "user");

        UserStorage.addUser(u1);
        UserStorage.addUser(u2);

        List<User> loaded = UserStorage.loadUsers();

        assertEquals(2, loaded.size());
    }

    @Test
    void testFindUserByEmail_Found() throws IOException {

        clearFile();

        User u1 = new User("Sara", "s@test.com", "999", "user");
        UserStorage.addUser(u1);

        User found = UserStorage.findUserByEmail("s@test.com");

        assertNotNull(found);
        assertEquals("Sara", found.fullName);
    }

    @Test
    void testFindUserByEmail_NotFound() throws IOException {

        clearFile();

        User u1 = new User("Sara", "s@test.com", "999", "user");
        UserStorage.addUser(u1);

        User found = UserStorage.findUserByEmail("nothing@test.com");

        assertNull(found);
    }

    @Test
    void testLoadUsersWhenFileEmpty() throws IOException {

        clearFile();

        List<User> users = UserStorage.loadUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}

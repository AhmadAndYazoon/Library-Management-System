package Library;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FineStorageTest {

    private static final String TEST_FILE = "src/test/resources/fines_test.txt";
    private static final Path fineFilePath = Paths.get(TEST_FILE);

    @BeforeAll
    static void useTestFile() {
        System.setProperty("fines.file", TEST_FILE);
    }

    @BeforeEach
    void cleanFile() throws IOException {
        Files.writeString(fineFilePath, "");
    }

    @Test
    void testLoadFines_EmptyWhenFileNotExistsOrEmpty() throws IOException {
        Files.deleteIfExists(fineFilePath);

        List<Fine> fines = FineStorage.loadFines();
        assertNotNull(fines);
        assertTrue(fines.isEmpty(), "المفروض يرجع ليست فاضية لما الملف مش موجود أو فاضي");
    }

    @Test
    void testUpdateFine_AddNewFine() {
        assertEquals(0.0, FineStorage.getFineAmount("user@test.com"));

        FineStorage.updateFine("user@test.com", 50.0);

        double amount = FineStorage.getFineAmount("user@test.com");
        assertEquals(50.0, amount);
    }

    @Test
    void testUpdateFine_UpdateExistingFine() {
        FineStorage.updateFine("user@test.com", 30.0);
        assertEquals(30.0, FineStorage.getFineAmount("user@test.com"));

        FineStorage.updateFine("user@test.com", 80.0);
        assertEquals(80.0, FineStorage.getFineAmount("user@test.com"));
    }

    @Test
    void testUpdateFine_RemoveFineWhenZero() {
        FineStorage.updateFine("user@test.com", 40.0);
        assertEquals(40.0, FineStorage.getFineAmount("user@test.com"));

        FineStorage.updateFine("user@test.com", 0.0);

        assertEquals(0.0, FineStorage.getFineAmount("user@test.com"));
    }
}

package Library;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CDStorageTest {

    private static final String TEST_FILE = "src/test/resources/cds_test.txt";
    private static final Path cdFilePath = Paths.get(TEST_FILE);

    @BeforeAll
    static void useTestFile() {
        System.setProperty("cds.file", TEST_FILE);
    }

    private void clearFile() throws IOException {
        Files.writeString(cdFilePath, "");
    }

    @Test
    void testSaveAndLoadCDs() throws IOException {
        clearFile();

        CD cd1 = new CD("CD1", "Author1", "111", false);
        CD cd2 = new CD("CD2", "Author2", "222", true);

        List<CD> cds = List.of(cd1, cd2);

        CDStorage.saveCDs(cds);

        assertTrue(Files.exists(cdFilePath));

        List<CD> loaded = CDStorage.loadCDs();

        assertEquals(2, loaded.size());

        CD lcd1 = loaded.get(0);
        assertEquals("CD1", lcd1.title);
        assertEquals("Author1", lcd1.author);
        assertEquals("111", lcd1.ISBN);
        assertFalse(lcd1.isBorrowed);

        CD lcd2 = loaded.get(1);
        assertEquals("CD2", lcd2.title);
        assertEquals("Author2", lcd2.author);
        assertEquals("222", lcd2.ISBN);
        assertTrue(lcd2.isBorrowed);
    }

    @Test
    void testLoadWhenFileEmpty() throws IOException {
        clearFile();

        List<CD> result = CDStorage.loadCDs();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

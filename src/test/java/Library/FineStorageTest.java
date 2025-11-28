package Library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FineStorageTest {

    @BeforeEach
    void resetFile() {
        FineStorage.saveFines(new ArrayList<>());
    }

    @Test
    void saveAndLoadFines_shouldPersistDataCorrectly() {
        List<Fine> fines = new ArrayList<>();
        fines.add(new Fine("a@test.com", 10.5));
        fines.add(new Fine("b@test.com", 20.0));

        FineStorage.saveFines(fines);

        List<Fine> loaded = FineStorage.loadFines();

        assertEquals(2, loaded.size());

        Fine f1 = loaded.get(0);
        assertEquals("a@test.com", f1.email);
        assertEquals(10.5, f1.amount);

        Fine f2 = loaded.get(1);
        assertEquals("b@test.com", f2.email);
        assertEquals(20.0, f2.amount);
    }

    @Test
    void getFineAmount_shouldReturnCorrectAmount() {
        List<Fine> fines = new ArrayList<>();
        fines.add(new Fine("user1@test.com", 15.0));
        fines.add(new Fine("user2@test.com", 25.0));

        FineStorage.saveFines(fines);

        assertEquals(15.0, FineStorage.getFineAmount("user1@test.com"));
        assertEquals(25.0, FineStorage.getFineAmount("user2@test.com"));
        assertEquals(0.0, FineStorage.getFineAmount("unknown@test.com")); // غير موجود
    }

    @Test
    void updateFine_shouldUpdateExistingFine() {
        List<Fine> fines = new ArrayList<>();
        fines.add(new Fine("user@test.com", 10.0));

        FineStorage.saveFines(fines);

        FineStorage.updateFine("user@test.com", 30.0);

        assertEquals(30.0, FineStorage.getFineAmount("user@test.com"));
    }

    @Test
    void updateFine_shouldAddNewFineIfNotExists() {

        FineStorage.updateFine("new@test.com", 50.0);

        List<Fine> loaded = FineStorage.loadFines();
        assertEquals(1, loaded.size());
        assertEquals("new@test.com", loaded.get(0).email);
        assertEquals(50.0, loaded.get(0).amount);
    }

    @Test
    void updateFine_shouldNotAddIfAmountIsZero() {
        FineStorage.updateFine("x@test.com", 0.0);

        List<Fine> loaded = FineStorage.loadFines();
        assertTrue(loaded.isEmpty());
    }
}

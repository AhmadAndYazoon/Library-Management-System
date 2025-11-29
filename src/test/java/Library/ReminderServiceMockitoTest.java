
package Library;

import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class ReminderServiceMockitoTest {

    @Test
    void testSendOverdueReminders_NotifiesObserverForOverdueUser() {

        Observer emailObserver = mock(Observer.class);

        User u1 = new User("Tareq", "t@test.com", "123", "user");

        Borrow overdueBorrow = new Borrow(
                "Book1",
                "Author",
                "111",
                "Tareq",
                "t@test.com",
                LocalDate.now().minusDays(3),   
                "BOOK"
        );

        try (MockedStatic<UserStorage> userMock = mockStatic(UserStorage.class);
             MockedStatic<BorrowStorage> borrowMock = mockStatic(BorrowStorage.class)) {

            userMock.when(UserStorage::loadUsers)
                    .thenReturn(List.of(u1));

            borrowMock.when(BorrowStorage::loadBorrowed)
                    .thenReturn(List.of(overdueBorrow));

            ReminderService service = new ReminderService();
            service.addObserver(emailObserver);

            service.sendOverdueReminders();

            
            verify(emailObserver, times(1))
                    .notify(eq(u1), eq("You have 1 overdue book(s)."));
        }
    }

    @Test
    void testSendOverdueReminders_DoesNotNotifyWhenBorrowNotOverdue() {

        Observer emailObserver = mock(Observer.class);

        User u1 = new User("Maya", "m@test.com", "123", "user");

        Borrow notOverdueBorrow = new Borrow(
                "Book1",
                "Author",
                "111",
                "Maya",
                "m@test.com",
                LocalDate.now().plusDays(5),  
                "BOOK"
        );

        try (MockedStatic<UserStorage> userMock = mockStatic(UserStorage.class);
             MockedStatic<BorrowStorage> borrowMock = mockStatic(BorrowStorage.class)) {

            userMock.when(UserStorage::loadUsers)
                    .thenReturn(List.of(u1));

            borrowMock.when(BorrowStorage::loadBorrowed)
                    .thenReturn(List.of(notOverdueBorrow));

            ReminderService service = new ReminderService();
            service.addObserver(emailObserver);

            service.sendOverdueReminders();

            verify(emailObserver, never()).notify(any(), anyString());
        }
    }
}



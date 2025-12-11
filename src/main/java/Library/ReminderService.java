package Library;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for sending reminder notifications
 * to users who have overdue borrowed items.
 *
 * <p>Implements the Observer pattern: observers (e.g. EmailNotifier)
 * are notified with a message for each user who has overdue items.</p>
 */
public class ReminderService {

    /** List of observers that should receive reminder notifications. */
    private List<Observer> observers = new ArrayList<>();

    /**
     * Registers a new observer to receive reminder notifications.
     *
     * @param obs observer implementation to add
     */
    public void addObserver(Observer obs) {
        observers.add(obs);
    }

    /**
     * Scans all users and borrows, detects overdue items,
     * and sends a notification to all registered observers
     * for each user that has one or more overdue borrows.
     */
    public void sendOverdueReminders() {

        List<User> users = UserStorage.loadUsers();
        List<Borrow> allBorrows = BorrowStorage.loadBorrowed();

        for (User user : users) {

            long overdueCount = allBorrows.stream()
                    .filter(b -> b.email.equalsIgnoreCase(user.email))
                    .filter(Borrow::isOverDueBorrow)
                    .count();

            if (overdueCount > 0) {

                String msg = "You have " + overdueCount + " overdue book(s).";

                for (Observer obs : observers) {
                    obs.notify(user, msg);
                }
            }
        }
    }
}

    

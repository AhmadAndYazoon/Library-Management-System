package Library;

import java.util.ArrayList;
import java.util.List;

public class ReminderService {

    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer obs) {
        observers.add(obs);
    }

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

    

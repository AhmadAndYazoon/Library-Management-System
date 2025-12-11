package Library;

/**
 * Observer interface used by the reminder system.
 * Implementations define how to deliver a reminder message to a user.
 */
public interface Observer {

    /**
     * Sends a reminder notification to the given user.
     *
     * @param user user to notify
     * @param msg  reminder message content
     */
    void notify(User user, String msg);
}

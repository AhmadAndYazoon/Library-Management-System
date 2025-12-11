package Library;

/**
 * Represents the accumulated fine amount for a single user.
 */
public class Fine {

    /** Email of the user who owns this fine. */
    public String email;

    /** Total fine amount in NIS. */
    public double amount;

    /**
     * Creates a new fine record.
     *
     * @param email  user email
     * @param amount fine amount in NIS
     */
    public Fine(String email, double amount) {
        this.email = email;
        this.amount = amount;
    }
}

package Library;

/**
 * Strategy interface for calculating fines for overdue media.
 */
public interface FineStrategy {

    /**
     * Calculates the fine amount for a given number of overdue days.
     *
     * @param overdueDays number of days the item is overdue
     * @return fine amount in NIS
     */
    int calculateFine(int overdueDays);
}

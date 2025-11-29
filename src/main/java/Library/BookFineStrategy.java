package Library;

/**
 * Fine calculation strategy for books.
 * Each overdue day costs 10 NIS.
 */
public class BookFineStrategy implements FineStrategy {

    /**
     * {@inheritDoc}
     * For books: fine = 10 * overdueDays.
     */
    @Override
    public int calculateFine(int overdueDays) {
        return 10 * overdueDays;
    }
}

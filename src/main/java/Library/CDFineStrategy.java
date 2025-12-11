package Library;

/**
 * Fine calculation strategy for CDs.
 * Each overdue day costs 20 NIS.
 */
public class CDFineStrategy implements FineStrategy {

    /**
     * {@inheritDoc}
     * For CDs: fine = 20 * overdueDays.
     */
    @Override
    public int calculateFine(int overdueDays) {
        return 20 * overdueDays;
    }
}

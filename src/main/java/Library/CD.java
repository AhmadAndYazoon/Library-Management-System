package Library;

import java.time.LocalDate;

/**
 * Represents a CD (audio or media disc) in the library collection.
 * A CD can be borrowed by a user for 7 days.
 */
public class CD extends Book {

    /**
     * Creates a new CD instance that is not borrowed.
     */
    public CD() {
        super();
    }

    /**
     * Creates a new CD with the given data.
     *
     * @param TITLE    CD title
     * @param AUTHOR   CD artist/author
     * @param ISBN     CD identifier/serial
     * @param BORROWED whether the CD is already borrowed
     */
    public CD(String TITLE, String AUTHOR, String ISBN, boolean BORROWED) {
        super(TITLE, AUTHOR, ISBN, BORROWED);
    }

    /**
     * Marks this CD as borrowed and sets its due date to 7 days from today.
     */
    @Override
    public void borrowBook() {
        isBorrowed = true;
        dueDate = LocalDate.now().plusDays(7);
    }
}

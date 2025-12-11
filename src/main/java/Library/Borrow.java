package Library;

import java.time.LocalDate;

/**
 * Represents a single borrow transaction of a book or CD by a user.
 */
public class Borrow {

    /** Identifier (ISBN/serial) of the borrowed item. */
    public String isbn;

    /** Full name of the user who borrowed the item. */
    public String username;

    /** Email of the user who borrowed the item. */
    public String email;

    /** Due date when the item should be returned. */
    public LocalDate dueDate;

    /** Author/artist of the borrowed item. */
    public String author;

    /** Title of the borrowed item. */
    public String title;

    /** Media type: typically "BOOK" or "CD". */
    public String mediaType;

    /**
     * Creates a new borrow record.
     *
     * @param title     item title
     * @param author    item author/artist
     * @param isbn      item identifier (ISBN/serial)
     * @param fullName  user full name
     * @param email     user email
     * @param dueDate   due date of the borrow
     * @param mediaType media type ("BOOK" or "CD")
     */
    public Borrow(String title, String author, String isbn,
                  String fullName, String email,
                  LocalDate dueDate, String mediaType) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.username = fullName;
        this.email = email;
        this.dueDate = dueDate;
        this.mediaType = mediaType;
    }

    /**
     * Checks whether this borrow is overdue relative to today's date.
     *
     * @return {@code true} if due date is before today; {@code false} otherwise
     */
    public boolean isOverDueBorrow() {
        return dueDate.isBefore(LocalDate.now());
    }
}

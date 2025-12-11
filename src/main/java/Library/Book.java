package Library;

import java.time.LocalDate;

/**
 * Represents a book in the library collection.
 * A book can be borrowed by a user for 28 days.
 */
public class Book {

    /** Book title. */
    public String title;

    /** Book author. */
    public String author;

    /** International Standard Book Number used as identifier. */
    public String ISBN;

    /** Flag that indicates whether this book is currently borrowed. */
    public boolean isBorrowed;

    /** Due date for returning the book, or {@code null} if not borrowed. */
    public LocalDate dueDate;

    /**
     * Creates a new book instance that is not borrowed.
     */
    public Book() {
        this.isBorrowed = false;
    }

    /**
     * Creates a new book with the given data.
     *
     * @param TITLE   book title
     * @param AUTHOR  book author
     * @param ISBN    book ISBN
     * @param BORROWED whether the book is already borrowed
     */
    public Book(String TITLE, String AUTHOR, String ISBN, boolean BORROWED) {
        this.title = TITLE;
        this.author = AUTHOR;
        this.ISBN = ISBN;
        this.isBorrowed = BORROWED;
        this.dueDate = null;
    }

    /**
     * Marks this book as borrowed and sets its due date to 28 days from today.
     */
    public void borrowBook() {
        isBorrowed = true;
        dueDate = LocalDate.now().plusDays(28);
    }

    /**
     * Marks this book as returned and clears the due date.
     */
    public void returnBook() {
        isBorrowed = false;
        dueDate = null;
    }
}


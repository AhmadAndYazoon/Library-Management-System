package Library;

import java.time.LocalDate;

public class Book {
    

    public String title;
    public String author;
    public String ISBN;
    public boolean isBorrowed;
    public LocalDate dueDate;

    public Book() {
        this.isBorrowed = false;
    }

    public Book(String TITLE, String AUTHOR, String ISBN, boolean BORROWED) {
        this.title = TITLE;
        this.author = AUTHOR;
        this.ISBN = ISBN;
        this.isBorrowed = BORROWED;
        this.dueDate = null;
    }

    public void borrowBook() {
        isBorrowed = true;
        dueDate = LocalDate.now().plusDays(28);
    }

    public void returnBook() {
        isBorrowed = false;
        dueDate = null;
    }
}

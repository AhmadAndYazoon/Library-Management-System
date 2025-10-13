package Library;

public class Book {
    public String title;
    public String author;
    public String ISBN;
    public boolean isBorrowed;

    
    public Book() {
        this.isBorrowed = false;
    }

    
    public Book(String TITLE, String AUTHOR, String ISBN, boolean BORROWED) {
        this.title = TITLE;
        this.author = AUTHOR;
        this.ISBN = ISBN;
        this.isBorrowed = BORROWED;
    }

    
    public void borrowBook() {
        isBorrowed = true;
    }

    public void returnBook() {
        isBorrowed = false;
    }
}

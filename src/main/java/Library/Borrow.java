
package Library;

import java.time.LocalDate;


public class Borrow {
    public String isbn;
    public String username;
    public String email;
    public LocalDate dueDate;
    public String author;
    public String title;
    public String mediaType;
    
    public Borrow(String title, String author, String isbn, String fullName, String email, LocalDate dueDate, String mediaType) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.username = fullName;
        this.email = email;
        this.dueDate = dueDate;
        this.mediaType = mediaType;
    }
    public boolean isOverDueBorrow() {
        return dueDate.isBefore(LocalDate.now());
    }
}


package Library;

import java.time.LocalDate;

public class CD extends Book {
    
    
    public CD() {
        super();
    }
    
    public CD(String TITLE, String AUTHOR, String ISBN, boolean BORROWED) {
        super(TITLE, AUTHOR, ISBN, BORROWED);
    }
    
    
    @Override
    public void borrowBook() {
        isBorrowed = true;
        dueDate = LocalDate.now().plusDays(7);
    }
    
    
    
}

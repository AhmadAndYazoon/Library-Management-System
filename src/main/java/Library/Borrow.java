
package Library;

import java.time.LocalDate;


public class Borrow {
    public String isbn;
    public String username;
    public String email;
    public LocalDate dueDate;
    public String author;
    public String title;
    
    public Borrow(String t,String auth,String i , String user , String em , LocalDate date) {
        title = t;
        author = auth;
        isbn = i;
        username = user;
        email = em;
        dueDate = date;
    }
    
    public boolean isOverDueBorrow() {
        return dueDate.isBefore(LocalDate.now());
    }
}

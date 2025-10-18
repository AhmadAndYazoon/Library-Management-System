package Library;

import java.io.Serializable;

public class User {
    public String fullName;
    public String email;
    public String password;
    public String role;

    public User(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}

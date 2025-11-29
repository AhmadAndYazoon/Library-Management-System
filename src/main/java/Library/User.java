package Library;

/**
 * Represents a system user (admin or regular library user).
 */
public class User {

    /** Full name of the user. */
    public String fullName;

    /** Unique email of the user, used for login and identification. */
    public String email;

    /** Plain-text password used in this academic project (not for production use). */
    public String password;

    /** User role, e.g., "admin" or "user". */
    public String role;

    /**
     * Creates a new user.
     *
     * @param fullName full name
     * @param email    email address
     * @param password password
     * @param role     role ("admin" or "user")
     */
    public User(String fullName, String email, String password, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}


package Library;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {

    // دايمًا نقرأ المسار من system properties
    private static String getFilePath() {
        return System.getProperty("users.file", "users.txt");
    }

    public static List<User> loadUsers() {

        List<User> users = new ArrayList<>();
        File file = new File(getFilePath());

        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                User u = new User(parts[0], parts[1], parts[2], parts[3]);
                users.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return users;
    }

    public static void saveUsers(List<User> users) {
        try (FileWriter writer = new FileWriter(new File(getFilePath()))) {
            for (User u : users) {
                String line = u.fullName + "," + u.email + "," + u.password + "," + u.role;
                writer.write(line + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(User user) {
        List<User> users = loadUsers();
        users.add(user);
        saveUsers(users);
    }

    public static User findUserByEmail(String email) {
        return loadUsers().stream()
                .filter(u -> u.email.equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}

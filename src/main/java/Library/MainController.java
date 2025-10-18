package Library;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController {
    @FXML private StackPane mainContent;
    @FXML private Stage stage;
    @FXML private Scene scene;
    @FXML private Parent root;
    @FXML private TextField fullnameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    

    public static String username;
    public static String email;
    
    @FXML
    private void initialize() {
        if(mainContent != null) {
            loadCenterContent("dashboard.fxml");
        }
    }
    
    
    @FXML
    private void handleRefreshUsers() {
        if (userTable != null) {
            ObservableList<User> userList = FXCollections.observableArrayList(UserStorage.loadUsers());
            nameColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().fullName));
            emailColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().email));
            roleColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().role));
            userTable.setItems(userList);
        }
    }

    

    @FXML
    private void handleRemoveUser() {
            if (userTable == null) return;

        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("⚠ No Selection", "Please select a user to remove.");
            return;
        }
        if (selected.role.equalsIgnoreCase("admin")) {
            showAlert("🚫 Restricted", "Admin accounts cannot be removed.");
            return;
        }
        
        double fineAmount = FineStorage.getFineAmount(selected.email);
        if (fineAmount > 0) {
            showAlert("🚫 Cannot Remove", "User has unpaid fines. Please clear them first.");
            return;
        }
        List<Borrow> borrows = BorrowStorage.loadBorrowed();
        boolean hasActiveLoans = borrows.stream()
                .anyMatch(b -> b.email.equalsIgnoreCase(selected.email));
        if (hasActiveLoans) {
            showAlert("🚫 Cannot Remove", "User has active borrowed books. Please return them first.");
            return;
        }
        
        List<User> users = UserStorage.loadUsers();
        users.removeIf(u -> u.email.equalsIgnoreCase(selected.email));
        UserStorage.saveUsers(users);

        borrows.removeIf(b -> b.email.equalsIgnoreCase(selected.email));
        BorrowStorage.saveBorrowed(borrows);

        
        handleRefreshUsers();
        showAlert("✅ User Removed", selected.fullName + " has been removed.");
    }



    
    
    @FXML
    private void handleSignUp(ActionEvent e) {
        String fullName = fullnameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("⚠ Missing Fields", "Please fill in all fields before signing up.");
            return;
        }
        if (UserStorage.findUserByEmail(email) != null) {
            showAlert("⚠ Duplicate Email", "This email is already registered.");
            return;
        }
        User newUser = new User(fullName, email, password, "user");
        UserStorage.addUser(newUser);
        showAlert("✅ Success", "Account created successfully! You can now log in.");
        SwitchToSignInForm(e);
    }

    @FXML
    private void handleLogin(ActionEvent e) {
        String email1 = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (email1.isEmpty() || password.isEmpty()) {
            showAlert("⚠ Missing Fields", "Please fill in all fields before logging in.");
            return;
        }
        List<User> users = UserStorage.loadUsers();
        for (User u : users) {
            if (u.email.equalsIgnoreCase(email1) && u.password.equals(password)) {
                if (u.role.equalsIgnoreCase("admin")) {
                    String str = "adminDashboard.fxml";
                    switchTo(str, e);
                } else {
                    username = u.fullName;
                    email = u.email;
                    String str2 = "userDashBoard.fxml";
                    switchTo(str2, e);
                }
                return;
            }
        }
        showAlert("❌ Invalid Login", "Email or password is incorrect.");
    }
    
    
    
    @FXML
    private void handleDashboard() {
        loadCenterContent("dashboard.fxml");
    }
    @FXML 
    private void handleManageBooks() {
        loadCenterContent("manageBooks.fxml");
    }
    @FXML 
    private void handleManageUsers() {
        handleRefreshUsers();
        loadCenterContent("manageUsers.fxml"); 
        
    }

    private void loadCenterContent(String fxmlFile) { 
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();
            mainContent.getChildren().clear();
            mainContent.getChildren().add(newContent);

            
            if (fxmlFile.contains("manageUsers.fxml")) {
                MainController controller = loader.getController();
                controller.handleRefreshUsers();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void SwitchToSignUpForm(ActionEvent event) {
        switchTo("signup.fxml", event);
    }

    @FXML
    private void SwitchToSignInForm(ActionEvent event) {
        switchTo("login.fxml", event);
    }

    private void switchTo(String path, ActionEvent ev) {
        try {
            root = FXMLLoader.load(getClass().getResource(path));
            stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose(MouseEvent event) {
        stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleMinimize(MouseEvent event) {
        stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleFullscreen(MouseEvent event) {
        stage = (Stage) ((Circle) event.getSource()).getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }

    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package Library;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class MainController {
    @FXML
    private StackPane mainContent;
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;


    @FXML
    private void SwitchToSignUpForm(ActionEvent event) {
        
        String str = "signup.fxml";
        switchTo(str, event);
    }
    @FXML
    private void SwitchToSignInForm(ActionEvent event) {
        
        String str = "login.fxml";
        switchTo(str, event);
    }
    
    @FXML
    private TextField fullnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    
    
    @FXML
    private void handleSignUp(ActionEvent e) {
        String username = fullnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Fields", "Please fill in all fields before Signing Up.");
            return;
        }
    }
    
    
    @FXML
    private void SwitchToAdmin(ActionEvent event) {
        String str = "adminDashboard.fxml";
        switchTo(str, event);
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
    
    @FXML
    private void initialize() {
        if (mainContent != null) {
            loadCenterContent("dashboard.fxml");
        }
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
        loadCenterContent("manageUsers.fxml");
    }
    private void loadCenterContent(String fxmlFile) {
        try {
            Parent newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(newContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout(ActionEvent ev) {
        SwitchToSignInForm(ev);
    }
    private void switchTo(String path,ActionEvent ev) {
        try {
            
            root = FXMLLoader.load(getClass().getResource(path));
            stage = (Stage) ((Node)ev.getSource()).getScene().getWindow();
            scene = new Scene(root);

            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

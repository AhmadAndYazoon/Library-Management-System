package Library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.List;

public class ManageCDsController {

    @FXML private TableView<CD> cdTable;
    @FXML private TableColumn<CD, String> titleColumn;
    @FXML private TableColumn<CD, String> authorColumn;
    @FXML private TableColumn<CD, String> isbnColumn;
    @FXML private TableColumn<CD, Boolean> borrowedColumn;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;

    private static final ObservableList<CD> cdList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {

       
        List<CD> loaded = CDStorage.loadCDs();
        cdList.setAll(loaded);

        // Setup Table Columns
        titleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
        authorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
        isbnColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().ISBN));
        borrowedColumn.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isBorrowed));

        borrowedColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean borrowed, boolean empty) {
                super.updateItem(borrowed, empty);
                setText(empty ? null : borrowed ? "Yes" : "No");
            }
        });

        cdTable.setItems(cdList);
    }

    @FXML
    private void handleAddCD() {
        String TITLE = titleField.getText().trim();
        String AUTHOR = authorField.getText().trim();
        String ISBN = isbnField.getText().trim();

        if (TITLE.isEmpty() || AUTHOR.isEmpty() || ISBN.isEmpty()) {
            showAlert("Missing Fields", "Please fill in all fields before adding a CD.");
            return;
        }

        boolean exists = cdList.stream().anyMatch(cd -> cd.ISBN.equalsIgnoreCase(ISBN));
        if (exists) {
            showAlert("Duplicate CD", "A CD with this Serial/ISBN already exists.");
            return;
        }

        CD newCD = new CD(TITLE, AUTHOR, ISBN, false);
        cdList.add(newCD);
        CDStorage.saveCDs(cdList);
        clearFields();
    }

    private void clearFields() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static ObservableList<CD> getCDList() {
        return cdList;
    }
}

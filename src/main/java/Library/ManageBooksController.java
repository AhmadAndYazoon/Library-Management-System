package Library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ManageBooksController {

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Boolean> borrowedColumn;

    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;

    
    private static final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().title));

        authorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().author));

        isbnColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().ISBN));

        borrowedColumn.setCellValueFactory(cellData ->
                new SimpleBooleanProperty(cellData.getValue().isBorrowed));

        
        borrowedColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean borrowed, boolean empty) {
                super.updateItem(borrowed, empty);
                setText(empty ? null : borrowed ? "Yes" : "No");
            }
        });

        bookTable.setItems(bookList);
    }

    @FXML
    private void handleAddBook() {
        String TITLE = titleField.getText().trim();
        String AUTHOR = authorField.getText().trim();
        String ISBN = isbnField.getText().trim();

        if (TITLE.isEmpty() || AUTHOR.isEmpty() || ISBN.isEmpty()) {
            showAlert("Missing Fields", "Please fill in all fields before adding a book.");
            return;
        }

        
        boolean exists = bookList.stream()
                .anyMatch(b -> b.ISBN.equalsIgnoreCase(ISBN));
        if (exists) {
            showAlert("Duplicate ISBN", "A book with this ISBN already exists.");
            return;
        }

        
        Book newBook = new Book(TITLE, AUTHOR, ISBN, false);
        bookList.add(newBook);
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

    
    public static ObservableList<Book> getBookList() {
        return bookList;
    }
}
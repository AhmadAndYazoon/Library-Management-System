package Library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
/**
 * JavaFX controller for all user-facing pages:
 * dashboard, my books, search books, my CDs, search CDs, and fine payment.
 *
 * <p>Contains the logic for borrowing items, searching, calculating fines,
 * and paying fines for the currently logged-in user.</p>
 */
public class UserController {

    @FXML private Stage stage;
    @FXML private Scene scene;
    @FXML private Parent root;

    @FXML private BorderPane page1;             // Dashboard
    @FXML private BorderPane booksroot;         // My Books
    @FXML private BorderPane page3;             // Search Books
    @FXML private BorderPane pageCDs;           // My CDs
    @FXML private BorderPane pageSearchCDs;     // Search CDs

    @FXML private Label borrowedCountLabel;
    @FXML private Label earliestDueLabel;
    @FXML private Label fineLabel;

    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Boolean> borrowedColumn;

    @FXML private TableView<Borrow> borrowTable;
    @FXML private TableColumn<Borrow, String> titleBColumn;
    @FXML private TableColumn<Borrow, String> authorBColumn;
    @FXML private TableColumn<Borrow, String> isbnBColumn;
    @FXML private TableColumn<Borrow, String> dueDateColumn;
    @FXML private TableColumn<Borrow, String> mediaTypeColumn;

    @FXML private TableView<CD> cdTable;
    @FXML private TableColumn<CD, String> cdTitleColumn2;
    @FXML private TableColumn<CD, String> cdArtistColumn2;
    @FXML private TableColumn<CD, String> cdSerialColumn2;
    @FXML private TableColumn<CD, Boolean> cdBorrowedColumn2;

    @FXML private TextField searchField;
    @FXML private TextField searchCDField;

    private static final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private static final ObservableList<Borrow> borrowedList = FXCollections.observableArrayList();
    private static final ObservableList<CD> cdList = FXCollections.observableArrayList();

    @FXML String Username = MainController.username;
    @FXML String email = MainController.email;

    @FXML
private void initialize() {

    
    if (page1 != null) {
        initDashboard();
        return;
    }

    
    if (booksroot != null) {
        initMyBooks();
        return;
    }

    // Search Books
    if (page3 != null) {
        initSearchBooks();
        return;
    }

    // My CDs
    if (pageCDs != null) {
        initMyCDs();
        return;
    }

    // Search CDs
    if (pageSearchCDs != null) {
        initSearchCDs();
    }
}   

private void initDashboard() {
    List<Borrow> all = BorrowStorage.loadBorrowed();
    List<Borrow> user = all.stream().filter(b -> b.email.equalsIgnoreCase(email)).toList();
    borrowedCountLabel.setText(String.valueOf(user.size()));

    user.stream().min((a, b) -> a.dueDate.compareTo(b.dueDate)).ifPresentOrElse(b -> {
        String title = b.mediaType.equals("BOOK")
                ? BookStorage.loadBooks().stream().filter(x -> x.ISBN.equals(b.isbn)).map(x -> x.title).findFirst().orElse("Unknown")
                : CDStorage.loadCDs().stream().filter(x -> x.ISBN.equals(b.isbn)).map(x -> x.title).findFirst().orElse("Unknown");
        earliestDueLabel.setText(title + " (Due: " + b.dueDate + ")");
    }, () -> earliestDueLabel.setText("No borrowed items"));
}


private void initMyBooks() {

    List<Borrow> loaded = BorrowStorage.loadBorrowed().stream()
            .filter(b -> b.email.equalsIgnoreCase(email) && b.mediaType.equals("BOOK"))
            .toList();

    borrowedList.setAll(loaded);

    titleBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
    authorBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
    isbnBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isbn));
    mediaTypeColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().mediaType));
    dueDateColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().dueDate.toString()));

    dueDateColumn.setCellFactory(col -> new TableCell<>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) { setText(null); setStyle(""); return; }

            Borrow b = getTableView().getItems().get(getIndex());
            setText(item);

            if (b.isOverDueBorrow())
                setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            else
                setStyle("-fx-text-fill: black;");
        }
    });

    double fineAmount = calculateFineForUser(email);
    FineStorage.updateFine(email, fineAmount);
    fineLabel.setText("Fine: " + fineAmount + " NIS");

    borrowTable.setItems(borrowedList);
}


private void initSearchBooks() {
    refreshBooks();
    titleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
    authorColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
    isbnColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().ISBN));
    borrowedColumn.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isBorrowed));
}

@FXML private TableView<CD> cdBorrowTable;
@FXML private TableColumn<CD, String> cdTitleColumn;
@FXML private TableColumn<CD, String> cdArtistColumn;
@FXML private TableColumn<CD, String> cdDueColumn;
@FXML private TableColumn<CD, String> cdSerialColumn;




private void initMyCDs() {

    // 1. Load all borrows for this user that are CDs
    List<Borrow> list = BorrowStorage.loadBorrowed().stream()
            .filter(b -> b.email.equalsIgnoreCase(email) && b.mediaType.equals("CD"))
            .toList();

    // 2. Convert Borrow → CD objects
    ObservableList<CD> cdBorrowed = FXCollections.observableArrayList();

    for (Borrow b : list) {
        CD cd = CDStorage.loadCDs().stream()
                .filter(c -> c.ISBN.equals(b.isbn))
                .findFirst()
                .orElse(null);

        if (cd != null) {
            // sync due date from borrow record
            cd.dueDate = b.dueDate;
            cdBorrowed.add(cd);
        }
    }

    // 3. Setup columns
    cdTitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
    cdArtistColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
    cdSerialColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().ISBN));
    cdDueColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().dueDate.toString()));

    // 4. Set the items
    cdBorrowTable.setItems(cdBorrowed);
}




private void initSearchCDs() {

    cdList.setAll(CDStorage.loadCDs());
    cdTable.setItems(cdList);

    cdTitleColumn2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
    cdArtistColumn2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
    cdSerialColumn2.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().ISBN));
    cdBorrowedColumn2.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isBorrowed));
}


    private double calculateFineForUser(String email) {
        List<Borrow> list = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email)).toList();

        double total = 0;

        for (Borrow b : list) {
            if (b.isOverDueBorrow()) {
                int d = (int) java.time.temporal.ChronoUnit.DAYS.between(b.dueDate, LocalDate.now());
                if (b.mediaType.equals("BOOK")) total += new BookFineStrategy().calculateFine(d);
                else total += new CDFineStrategy().calculateFine(d);
            }
        }
        return total;
    }

    @FXML
    private void handleSearchCD() {
        String q = searchCDField.getText().toLowerCase();

        ObservableList<CD> all = FXCollections.observableArrayList(CDStorage.loadCDs());

        ObservableList<CD> filtered = all.filtered(cd ->
                cd.title.toLowerCase().contains(q) ||
                cd.author.toLowerCase().contains(q) ||
                cd.ISBN.toLowerCase().contains(q)
        );

        cdTable.setItems(filtered);
    }

    @FXML
    private void handleBorrowCD() {

        double fine = FineStorage.getFineAmount(email);
        boolean overdue = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email))
                .anyMatch(Borrow::isOverDueBorrow);

        if (fine > 0 || overdue) {
            MainController.showAlert("Restricted", "You cannot borrow because you have overdue items or unpaid fines.");
            return;
        }

        CD cd = cdTable.getSelectionModel().getSelectedItem();
        if (cd == null) {
            MainController.showAlert("No Selection", "Select a CD first.");
            return;
        }

        if (cd.isBorrowed) {
            MainController.showAlert("Already Borrowed", "This CD is already borrowed.");
            return;
        }

        cd.borrowBook();

        List<CD> cds = CDStorage.loadCDs();
        for (CD c : cds) {
            if (c.ISBN.equals(cd.ISBN)) {
                c.isBorrowed = true;
                c.dueDate = cd.dueDate;
            }
        }
        CDStorage.saveCDs(cds);

        BorrowStorage.addBorrow(new Borrow(
                cd.title, cd.author, cd.ISBN,
                Username, email, cd.dueDate, "CD"
        ));

        cdList.setAll(CDStorage.loadCDs());
        cdTable.refresh();

        MainController.showAlert("Success", "CD Borrowed until " + cd.dueDate);
    }

    @FXML
    private void handleSearch() {
        String q = searchField.getText().toLowerCase();
        ObservableList<Book> f = bookList.filtered(b ->
                b.title.toLowerCase().contains(q) ||
                b.author.toLowerCase().contains(q) ||
                b.ISBN.toLowerCase().contains(q)
        );
        bookTable.setItems(f);
    }

    @FXML
    private void handleBorrow() {

        double fine = FineStorage.getFineAmount(email);
        boolean overdue = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email))
                .anyMatch(Borrow::isOverDueBorrow);

        if (fine > 0 || overdue) {
            MainController.showAlert("Restricted", "You have overdue items or unpaid fines.");
            return;
        }

        Book book = bookTable.getSelectionModel().getSelectedItem();
        if (book == null) {
            MainController.showAlert("No Selection", "Select a book first.");
            return;
        }

        if (book.isBorrowed) {
            MainController.showAlert("Borrowed", "This book is already borrowed.");
            return;
        }

        book.borrowBook();

        List<Book> all = BookStorage.loadBooks();
        for (Book b : all) {
            if (b.ISBN.equals(book.ISBN)) {
                b.isBorrowed = true;
                b.dueDate = book.dueDate;
            }
        }
        BookStorage.saveBooks(all);

        BorrowStorage.addBorrow(new Borrow(
                book.title, book.author, book.ISBN,
                Username, email, book.dueDate, "BOOK"
        ));

        refreshBooks();
        MainController.showAlert("Success", "Borrowed until " + book.dueDate);
    }

    private void refreshBooks() {
        bookList.setAll(BookStorage.loadBooks());
        bookTable.setItems(bookList);
    }

    @FXML private void SwitchToPage1(ActionEvent e) { switchTo("userDashBoard.fxml", e); }
    @FXML private void SwitchToPage2(ActionEvent e) {
    
    switchTo("UserBooks.fxml", e);
}

    @FXML private void SwitchToPage3(ActionEvent e) { switchTo("UserSearchbook.fxml", e); }
    @FXML private void SwitchToCDs(ActionEvent e) { switchTo("UserCDs.fxml", e); }
    @FXML private void SwitchToSearchCDs(ActionEvent e) { switchTo("UserSearchCD.fxml", e); }
    @FXML private void handleLogout(ActionEvent e) { switchTo("login.fxml", e); }

    private void switchTo(String fxml, ActionEvent e) {
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ignored) {
            ignored.printStackTrace(); 
        }
    }

    
    
    
    @FXML
private void handlePayFine() {

    double totalFine = FineStorage.getFineAmount(email);
    if (totalFine <= 0) {
        MainController.showAlert("No Fine", "You don't have any fines.");
        return;
    }

    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Pay Fine");
    dialog.setHeaderText("Total fine: " + totalFine + " NIS");
    dialog.setContentText("Enter amount to pay:");

    Optional<String> result = dialog.showAndWait();
    if (result.isEmpty()) return;

    double amount;
    try {
        amount = Double.parseDouble(result.get());
        if (amount <= 0) return;
    } catch (Exception e) {
        return;
    }

    double remaining = totalFine - amount;
    if (remaining < 0) remaining = 0;

    FineStorage.updateFine(email, remaining);

    if (remaining == 0) {

        List<Borrow> all = BorrowStorage.loadBorrowed();
        for (Borrow b : all) {
            if (b.email.equalsIgnoreCase(email) && b.isOverDueBorrow())
                b.dueDate = LocalDate.now().plusDays(28);
        }

        BorrowStorage.saveBorrowed(all);

        if (borrowTable != null)
            borrowTable.refresh();

        if (cdBorrowTable != null)
            cdBorrowTable.refresh();
    }

    if (fineLabel != null)
        fineLabel.setText("Fine: " + remaining + " NIS");
}

    
    
    
    @FXML private void handleClose(MouseEvent e) {
        stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML private void handleMinimize(MouseEvent e) {
        stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML private void handleFullscreen(MouseEvent e) {
        stage = (Stage) ((Circle) e.getSource()).getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());
    }
}

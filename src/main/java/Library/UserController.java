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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UserController {

    @FXML private StackPane mainContent;
    @FXML private Stage stage;
    @FXML private Scene scene;
    @FXML private Parent root;
    @FXML private Label borrowedCountLabel;
    @FXML private Label earliestDueLabel;
    @FXML private Label fineLabel;
    @FXML private BorderPane page1;
    @FXML private BorderPane page2;
    @FXML private BorderPane page3;
    
    
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
    
    
    
    @FXML private TextField searchField;
    private static final ObservableList<Book> bookList = FXCollections.observableArrayList();
    private static final ObservableList<Borrow> borrwedList = FXCollections.observableArrayList();
    @FXML String Username = MainController.username;
    @FXML String email = MainController.email;
    
    
    @FXML
    private void initialize() {
        if(page1 != null) {
            List<Borrow> allBorrows = BorrowStorage.loadBorrowed();
            
            List<Borrow> userBorrows = allBorrows.stream()
                    .filter(b -> b.email.equalsIgnoreCase(email))
                    .toList();

            
            borrowedCountLabel.setText(String.valueOf(userBorrows.size()));

            
            userBorrows.stream()
                    .min((b1, b2) -> b1.dueDate.compareTo(b2.dueDate))
                    .ifPresentOrElse(earliest -> {
                        
                        String bookTitle = FileStorage.loadBooks().stream()
                                .filter(book -> book.ISBN.equals(earliest.isbn))
                                .map(book -> book.title)
                                .findFirst()
                                .orElse("Unknown Book");


                        earliestDueLabel.setText(bookTitle + " (Due: " + earliest.dueDate + ")");
                    }, () -> {
                        earliestDueLabel.setText("No borrowed books");
                    });
        } else if (page2 != null) {
            
            List<Borrow> loaded = BorrowStorage.loadBorrowed().stream()
            .filter(b -> b.email.equalsIgnoreCase(email))
            .toList();
            
            borrwedList.setAll(loaded);
            
            titleBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().title));
            authorBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().author));
            isbnBColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().isbn));
            dueDateColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().dueDate.toString()));
            
            
            dueDateColumn.setCellFactory(col -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        int rowIndex = getIndex();
                        if (rowIndex >= 0 && rowIndex < getTableView().getItems().size()) {
                            Borrow borrow = getTableView().getItems().get(rowIndex);
                            setText(item);

                            if (borrow.isOverDueBorrow()) {
                                setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                            } else {
                                setStyle("-fx-text-fill: black;");
                            }
                        } else {
                            setText(null);
                            setStyle("");
                        }
                    }
                }
                }); 
            double fineAmount = calculateFineForUser(email);
            FineStorage.updateFine(email, fineAmount);
            fineLabel.setText("Fine: " + fineAmount + " NIS");
            borrowTable.setItems(borrwedList);
            
        } else {
            
            refreshBooks();
            
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

            
        }
    }
    
    
    private boolean hasBorrowRestrictions(String email) {
        List<Borrow> userBorrows = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email))
                .toList();


        boolean hasOverdue = userBorrows.stream().anyMatch(Borrow::isOverDueBorrow);

        return hasOverdue;
        }
    
    private double calculateFineForUser(String email) {
        List<Borrow> userBorrows = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email))
                .toList();

        double totalFine = 0;
        for (Borrow b : userBorrows) {
            if (b.isOverDueBorrow()) {
                long overdueDays = java.time.temporal.ChronoUnit.DAYS.between(b.dueDate, java.time.LocalDate.now());
                totalFine += overdueDays * 10;
            }
        }
        return totalFine;
    }   
    
    @FXML
    private void handlePayFine() {
        double totalFine = FineStorage.getFineAmount(email);
        if (totalFine <= 0) {
            MainController.showAlert("✅ No Fine", "You don't have any fines to pay.");
            return;
        }

        
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Pay Fine");
        dialog.setHeaderText("Total fine: " + totalFine + " NIS");
        dialog.setContentText("Enter amount to pay:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        double amountToPay;
        try {
            amountToPay = Double.parseDouble(result.get());
            if (amountToPay <= 0) {
                MainController.showAlert("❌ Invalid Amount", "Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            MainController.showAlert("❌ Invalid Input", "Please enter a valid number.");
            return;
        }

        
        double remainingFine = totalFine - amountToPay;
        if (remainingFine < 0) remainingFine = 0;

        
        FineStorage.updateFine(email, remainingFine);

        
        if (remainingFine == 0) {
            List<Borrow> borrows = BorrowStorage.loadBorrowed();
            for (Borrow b : borrows) {
                if (b.email.equalsIgnoreCase(email) && b.isOverDueBorrow()) {
                    b.dueDate = LocalDate.now().plusDays(28);
                }
            }
            BorrowStorage.saveBorrowed(borrows);
            borrwedList.setAll(
                BorrowStorage.loadBorrowed().stream()
                    .filter(b -> b.email.equalsIgnoreCase(email))
                    .toList()
            );
            borrowTable.refresh();
            MainController.showAlert("✅ Fine Paid", "Your fine has been fully paid. You can borrow again.");
        } else {
            MainController.showAlert("✅ Partial Payment",
                "You paid " + amountToPay + " NIS.\nRemaining fine: " + remainingFine + " NIS.");
        }

        
        fineLabel.setText("Fine: " + remainingFine + " NIS");
    }
    
    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ObservableList<Book> filtered = bookList.filtered(b ->
            b.title.toLowerCase().contains(query) ||
            b.author.toLowerCase().contains(query) ||
            b.ISBN.toLowerCase().contains(query)
        );
        bookTable.setItems(filtered);
    }
    
    @FXML
    private void handleBorrow() {
        double fine = FineStorage.getFineAmount(email);
        boolean hasOverdue = BorrowStorage.loadBorrowed().stream()
                .filter(b -> b.email.equalsIgnoreCase(email))
                .anyMatch(Borrow::isOverDueBorrow);

        if (fine > 0 || hasOverdue) {
            MainController.showAlert("Borrow Restricted 🚫", 
                "You cannot borrow new books because you have overdue books or unpaid fines.");
            return;
        }
        Book book = bookTable.getSelectionModel().getSelectedItem();
        if(book == null) {
            MainController.showAlert("⚠ No Selection", "Please select a Book to Borrow.");
            return;
        }
        
        if(  book.isBorrowed == true ) {
            MainController.showAlert("Book is Already Borrowed", "Sorry That Book Has Been Borrowed Already .");
            return;
        }
        
        book.borrowBook();
        
        List<Book> allBooks = FileStorage.loadBooks();
        for (Book b : allBooks) {
            if ( b.ISBN.equals(book.ISBN)) {
                b.isBorrowed = true;
                b.dueDate = book.dueDate;
                break;
            }
        }
        FileStorage.saveBooks(allBooks);
        
        BorrowStorage.addBorrow(new Borrow(book.title,book.author,book.ISBN,Username , email, book.dueDate));
        bookList.setAll(FileStorage.loadBooks());
        bookTable.refresh();
        MainController.showAlert("✅ Success", "Book borrowed successfully until " + book.dueDate + ".");

        
    }
    
    private void refreshBooks() {
        List<Book> loaded = FileStorage.loadBooks();
        bookList.setAll(loaded);
    }

    @FXML
    private void SwitchToPage1(ActionEvent ev) {
        String str = "userDashBoard.fxml";
        switchTo(str, ev);
    }
    @FXML
    private void SwitchToPage2(ActionEvent ev) {
        String str = "UserBooks.fxml";
        switchTo(str, ev);
    }
    @FXML
    private void SwitchToPage3(ActionEvent ev) {
        String str = "UserSearchbook.fxml";
        switchTo(str, ev);
    }

    @FXML
    private void handleLogout(ActionEvent e) {
        switchTo("login.fxml", e);
    }

    private void loadCenterContent(String fxmlFile) {
        try {
            Parent newContent = FXMLLoader.load(getClass().getResource(fxmlFile));
            mainContent.getChildren().clear();
            mainContent.getChildren().add(newContent);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void switchTo(String path, ActionEvent ev) {
        try {
            root = FXMLLoader.load(getClass().getResource(path));
            stage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
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
    
    
}

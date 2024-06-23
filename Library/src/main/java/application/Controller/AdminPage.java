package application.Controller;

import application.Model.Book;
import application.Util.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminPage {

    @FXML
    private TextField bookTitleField;

    @FXML
    private TextField authorField;

    @FXML
    private Button addBookButton;

    @FXML
    private Button editBookButton;

    @FXML
    private Button deleteBookButton;

    @FXML
    private Button lendBookButton;

    @FXML
    private TableView<Book> bookTableView;

    @FXML
    private TableColumn<Book, Integer> idColumn;

    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    private ObservableList<Book> bookList = FXCollections.observableArrayList();

    private Connection connection;

    @FXML
    private TextField editBookTitleField;

    @FXML
    private TextField editAuthorField;

    @FXML
    private Button updateBookButton;

    @FXML
    public void initialize() {
        try {
            connection = DB.getConnection();
            loadBooks();

            // Initialize the TableView columns
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

            // Set the book list to the TableView
            bookTableView.setItems(bookList);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database connection error!");
        }
    }

    private void loadBooks() {
        String query = "SELECT * FROM book";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            bookList.clear(); // Clear existing data

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");

                Book book = new Book(id, title, author);
                bookList.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error loading books: " + e.getMessage());
        }
    }

    @FXML
    void addBook() {
        String title = bookTitleField.getText().trim();
        String author = authorField.getText().trim();

        if (title.isEmpty() || author.isEmpty()) {
            showErrorAlert("Title and author fields cannot be empty!");
            return;
        }

        // Insert book into database
        String insertQuery = "INSERT INTO book (title, author) VALUES (?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(insertQuery);
            statement.setString(1, title);
            statement.setString(2, author);
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                showInfoAlert("Book added successfully!");
                loadBooks(); // Refresh the book list after adding a new book
            } else {
                showErrorAlert("Failed to add book!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error adding book: " + e.getMessage());
        }
    }

    @FXML
    void editBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showErrorAlert("Please select a book to update!");
            return;
        }

        String newTitle = editBookTitleField.getText().trim();
        String newAuthor = editAuthorField.getText().trim();

        if (newTitle.isEmpty() || newAuthor.isEmpty()) {
            showErrorAlert("Title and author fields cannot be empty!");
            return;
        }

        String updateQuery = "UPDATE book SET title = ?, author = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            statement.setString(1, newTitle);
            statement.setString(2, newAuthor);
            statement.setInt(3, selectedBook.getId());

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                showInfoAlert("Book updated successfully!");
                loadBooks(); // Refresh the book list after updating
            } else {
                showErrorAlert("Failed to update book!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error updating book: " + e.getMessage());
        }
    }

    @FXML
    void deleteBook() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showErrorAlert("Please select a book to delete!");
            return;
        }

        String deleteQuery = "DELETE FROM book WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1, selectedBook.getId());

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                showInfoAlert("Book deleted successfully!");
                loadBooks(); // Refresh the book list after deleting
            } else {
                showErrorAlert("Failed to delete book!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Error deleting book: " + e.getMessage());
        }
    }


    @FXML
    void lendBook() {
        // Implement lend book functionality
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
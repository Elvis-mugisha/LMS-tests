package application.Controller;

import application.Model.Book;
import application.Util.DB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private AdminPage adminPage;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize JavaFX runtime
        new JFXPanel();  //  initialize JavaFX toolkit

        // Ensure that all JavaFX related code is run on the FX Application Thread
        Platform.runLater(() -> {
            MockitoAnnotations.openMocks(this);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
            when(preparedStatement.executeUpdate()).thenReturn(1);

            adminPage.bookTitleField = new TextField();
            adminPage.authorField = new TextField();
            adminPage.editBookTitleField = new TextField();
            adminPage.editAuthorField = new TextField();
            adminPage.bookTableView = new TableView<>();
            adminPage.idColumn = new TableColumn<>();
            adminPage.titleColumn = new TableColumn<>();
            adminPage.authorColumn = new TableColumn<>();
            adminPage.bookList = FXCollections.observableArrayList();
        });
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            // Arrange
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("title")).thenReturn("Test Title");
            when(resultSet.getString("author")).thenReturn("Test Author");

            // Act
            adminPage.initialize();

            // Assert
            verify(connection, times(1)).prepareStatement("SELECT * FROM book");
            verify(preparedStatement, times(1)).executeQuery();
            verify(resultSet, times(1)).next();
            assertEquals(1, adminPage.bookList.size());
        });
    }

    @Test
    void testAddBookSuccess() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            adminPage.bookTitleField.setText("New Book");
            adminPage.authorField.setText("New Author");

            // Act
            adminPage.addBook();

            // Assert
            verify(preparedStatement, times(1)).executeUpdate();
            verify(preparedStatement).setString(1, "New Book");
            verify(preparedStatement).setString(2, "New Author");
            assertEquals(0, adminPage.bookTitleField.getText().length());
            assertEquals(0, adminPage.authorField.getText().length());
        });
    }

    @Test
    void testAddBookFailure() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            adminPage.bookTitleField.setText("New Book");
            adminPage.authorField.setText("New Author");
            when(preparedStatement.executeUpdate()).thenReturn(0);

            // Act
            adminPage.addBook();

            // Assert
            verify(preparedStatement, times(1)).executeUpdate();
            // Add more assertions as needed to verify the behavior
        });
    }

    @Test
    void testEditBookSuccess() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            Book book = new Book(1, "Original Title", "Original Author");
            adminPage.bookList.add(book);
            adminPage.bookTableView.setItems(adminPage.bookList);
            adminPage.bookTableView.getSelectionModel().select(book);
            adminPage.editBookTitleField.setText("Updated Title");
            adminPage.editAuthorField.setText("Updated Author");

            // Act
            adminPage.editBook();

            // Assert
            verify(preparedStatement, times(1)).executeUpdate();
            verify(preparedStatement).setString(1, "Updated Title");
            verify(preparedStatement).setString(2, "Updated Author");
            verify(preparedStatement).setInt(3, 1);
            assertEquals(0, adminPage.editBookTitleField.getText().length());
            assertEquals(0, adminPage.editAuthorField.getText().length());
        });
    }

    @Test
    void testDeleteBookSuccess() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            Book book = new Book(1, "Book Title", "Book Author");
            adminPage.bookList.add(book);
            adminPage.bookTableView.setItems(adminPage.bookList);
            adminPage.bookTableView.getSelectionModel().select(book);

            // Act
            adminPage.deleteBook();

            // Assert
            verify(preparedStatement, times(1)).executeUpdate();
            verify(preparedStatement).setInt(1, 1);
            assertEquals(0, adminPage.bookList.size());
        });
    }

    @Test
    void testDeleteBookFailure() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            Book book = new Book(1, "Book Title", "Book Author");
            adminPage.bookList.add(book);
            adminPage.bookTableView.setItems(adminPage.bookList);
            adminPage.bookTableView.getSelectionModel().select(book);
            when(preparedStatement.executeUpdate()).thenReturn(0);

            // Act
            adminPage.deleteBook();

            // Assert
            verify(preparedStatement, times(1)).executeUpdate();
            // Add more assertions as needed to verify the behavior
        });
    }

    // Additional test cases for other methods and error scenarios can be added similarly
}

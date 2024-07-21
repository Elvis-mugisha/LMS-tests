package application.controller;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @InjectMocks
    private UserPage userPage;

    @BeforeEach
    void setUp() throws SQLException {
        // Initialize JavaFX runtime
        new JFXPanel();  // Initialize JavaFX toolkit

        // Ensure that all JavaFX related code is run on the FX Application Thread
        Platform.runLater(() -> {
            MockitoAnnotations.openMocks(this);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);

            userPage.bookTableView = new TableView<>();
            userPage.titleColumn = new TableColumn<>();
            userPage.authorColumn = new TableColumn<>();
            userPage.bookList = FXCollections.observableArrayList();

            // Initialize TableView columns
            userPage.titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            userPage.authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        });
    }

    @Test
    void testInitializeSuccess() {
        Platform.runLater(() -> {
            // Arrange
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("title")).thenReturn("Test Title");
            when(resultSet.getString("author")).thenReturn("Test Author");

            // Act
            userPage.initialize();

            // Assert
            verify(connection, times(1)).prepareStatement("SELECT * FROM book");
            verify(preparedStatement, times(1)).executeQuery();
            verify(resultSet, times(1)).next();
            assertEquals(1, userPage.bookList.size());
            assertEquals("Test Title", userPage.bookList.get(0).getTitle());
            assertEquals("Test Author", userPage.bookList.get(0).getAuthor());
        });
    }

    @Test
    void testInitializeFailure() {
        Platform.runLater(() -> {
            // Arrange
            when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

            // Act
            userPage.initialize();

            // Assert
            verify(connection, times(1)).prepareStatement("SELECT * FROM book");
            assertEquals(0, userPage.bookList.size());
        });
    }

    @Test
    void testBorrowBookSuccess() {
        Platform.runLater(() -> {
            // Arrange
            Book book = new Book(1, "Test Title", "Test Author");
            userPage.bookList.add(book);
            userPage.bookTableView.setItems(userPage.bookList);
            userPage.bookTableView.getSelectionModel().select(book);

            // Act
            userPage.borrowBook();

            // Assert
            assertEquals("Test Title", userPage.bookTableView.getSelectionModel().getSelectedItem().getTitle());
        });
    }

    @Test
    void testBorrowBookFailure() {
        Platform.runLater(() -> {
            // Arrange
            // No book is selected

            // Act
            userPage.borrowBook();

            // Assert
            assertNull(userPage.bookTableView.getSelectionModel().getSelectedItem());
        });
    }

    @Test
    void testLoadBooksSuccess() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            when(resultSet.next()).thenReturn(true, false);
            when(resultSet.getInt("id")).thenReturn(1);
            when(resultSet.getString("title")).thenReturn("Test Title");
            when(resultSet.getString("author")).thenReturn("Test Author");

            // Act
            userPage.loadBooks();

            // Assert
            verify(preparedStatement, times(1)).executeQuery();
            assertEquals(1, userPage.bookList.size());
            assertEquals("Test Title", userPage.bookList.get(0).getTitle());
            assertEquals("Test Author", userPage.bookList.get(0).getAuthor());
        });
    }

    @Test
    void testLoadBooksFailure() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            when(preparedStatement.executeQuery()).thenThrow(SQLException.class);

            // Act
            userPage.loadBooks();

            // Assert
            verify(preparedStatement, times(1)).executeQuery();
            assertEquals(0, userPage.bookList.size());
        });
    }
}

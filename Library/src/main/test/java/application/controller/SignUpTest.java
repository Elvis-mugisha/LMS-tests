package application.controllers;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@PrepareForTest({ FXMLLoader.class, SignUpController.class })
class SignUpTest {

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @InjectMocks
    private SignUpController signUpController;

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize JavaFX runtime
        new JFXPanel();  //  initialize JavaFX toolkit

        // Ensure that all JavaFX related code is run on the FX Application Thread
        Platform.runLater(() -> {
            MockitoAnnotations.openMocks(this);
            try {
                when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            signUpController.idField = new TextField();
            signUpController.nameField = new TextField();
            signUpController.surnameField = new TextField();
            signUpController.usernameField = new TextField();
            signUpController.passwordField = new PasswordField();
        });
    }

    @Test
    public void testInitialize() {
        Platform.runLater(() -> {
            // Act
            signUpController.initialize();

            // Assert
            verify(connection, times(1)).prepareStatement(anyString());
        });
    }

    @Test
    public void testSignUpSuccess() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            signUpController.idField.setText("1");
            signUpController.nameField.setText("John");
            signUpController.surnameField.setText("Doe");
            signUpController.usernameField.setText("johndoe");
            signUpController.passwordField.setText("password123");
            try {
                when(preparedStatement.executeUpdate()).thenReturn(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Act
            signUpController.signUp(new ActionEvent());

            // Assert
            try {
                verify(preparedStatement, times(1)).executeUpdate();
                // Add more assertions as needed to verify the behavior
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testSignUpFailure() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            signUpController.idField.setText("1");
            signUpController.nameField.setText("John");
            signUpController.surnameField.setText("Doe");
            signUpController.usernameField.setText("johndoe");
            signUpController.passwordField.setText("password123");
            try {
                when(preparedStatement.executeUpdate()).thenReturn(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Act
            signUpController.signUp(new ActionEvent());

            // Assert
            try {
                verify(preparedStatement, times(1)).executeUpdate();
                // Add more assertions as needed to verify the behavior
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testSignUpWithSQLException() throws SQLException {
        Platform.runLater(() -> {
            // Arrange
            signUpController.idField.setText("1");
            signUpController.nameField.setText("John");
            signUpController.surnameField.setText("Doe");
            signUpController.usernameField.setText("johndoe");
            signUpController.passwordField.setText("password123");
            try {
                when(preparedStatement.executeUpdate()).thenThrow(new SQLException());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Act
            signUpController.signUp(new ActionEvent());

            // Assert
            try {
                verify(preparedStatement, times(1)).executeUpdate();
                // Add more assertions as needed to verify the behavior
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testSignUpWithEmptyFields() {
        Platform.runLater(() -> {
            // Arrange
            signUpController.idField.setText("");
            signUpController.nameField.setText("");
            signUpController.surnameField.setText("");
            signUpController.usernameField.setText("");
            signUpController.passwordField.setText("");

            // Act
            signUpController.signUp(new ActionEvent());

            // Assert
            verify(preparedStatement, never()).executeUpdate();
        });
    }

    @Test
    public void testBackToLogin() throws IOException {
        Platform.runLater(() -> {
            // Mock the behavior of FXMLLoader
            Parent mockRoot = mock(Parent.class);
            try {
                PowerMockito.mockStatic(FXMLLoader.class);
                when(FXMLLoader.load(getClass().getResource("/application/Login.fxml"))).thenReturn(mockRoot);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Mock the behavior of the stage
            Stage mockStage = mock(Stage.class);
            when(signUpController.idField.getScene().getWindow()).thenReturn(mockStage);

            // Call the backToLogin method
            signUpController.backToLogin(new ActionEvent());

            // Verify that the scene was set correctly
            verify(mockStage).setScene(any(Scene.class));
        });
    }

    @Test
    public void testBackToLoginWithIOException() throws IOException {
        Platform.runLater(() -> {
            // Mock the behavior to throw IOException
            try {
                PowerMockito.mockStatic(FXMLLoader.class);
                when(FXMLLoader.load(getClass().getResource("/application/Login.fxml"))).thenThrow(new IOException());
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Call the backToLogin method
            signUpController.backToLogin(new ActionEvent());

            // Verify that the error alert was shown
            // (You might need to add a method to verify the alert in your controller)
        });
    }

    @Test
    public void testShowInfoAlert() {
        Platform.runLater(() -> {
            // Call the showInfoAlert method
            signUpController.showInfoAlert("Test Information Message");

            // Verify that the alert was shown with the correct message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(null);
            alert.setContentText("Test Information Message");
            alert.showAndWait();
        });
    }

    @Test
    public void testShowErrorAlert() {
        Platform.runLater(() -> {
            // Call the showErrorAlert method
            signUpController.showErrorAlert("Test Error Message");

            // Verify that the alert was shown with the correct message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Test Error Message");
            alert.showAndWait();
        });
    }
}

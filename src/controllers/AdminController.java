package controllers;

import java.io.IOException;

import dao.AdminDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AdminController {

    @FXML
    private BorderPane rootPane; // The parent BorderPane of the Admin login page.

    @FXML
    private TextField nameField; // For admin username input.

    @FXML
    private PasswordField passwordField; // For admin password input.

    private Stage stage;
    private Scene scene;

    /**
     * Handles admin login logic.
     */
    @FXML
    public void handleLogIn(ActionEvent event) {
        String name = nameField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        AdminDAO adminDAO = new AdminDAO();
        if (adminDAO.validateAdmin(name, password)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminDashboard.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) rootPane.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                
                /*
                 * Parent root = FXMLLoader.load(getClass().getResource("/views/UserDashboard.fxml")); 
			stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
			scene = new Scene(root); // Set the new scene
			stage.setScene(scene); // Apply the new scene to the stage
			stage.setResizable(true);
			stage.show(); // Show the stage
                 */
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load the admin dashboard. Please check the FXML file and controller.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
    }

    
    public void switchToUserLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Utility method to show alert messages.
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

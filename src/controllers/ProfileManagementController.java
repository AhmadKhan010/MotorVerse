package controllers;

import java.io.IOException;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.User;
import utils.SessionManager;

public class ProfileManagementController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField passwordField;
    @FXML private BorderPane rootPane;

    private UserDAO userDAO;
    private User loggedInUser;

    @FXML
    public void initialize() {
        userDAO = new UserDAO();
        String username = SessionManager.getInstance().getLoggedInUser();
        loggedInUser = userDAO.getUser(username);
        if (loggedInUser != null) {
            nameField.setText(loggedInUser.getName());
            emailField.setText(loggedInUser.getEmail());
            phoneField.setText(loggedInUser.getPhoneNumber());
            addressField.setText(loggedInUser.getAddress());
            passwordField.setText(loggedInUser.getPassword());
        } else {
            showAlert("Error", "Unable to load user data.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSaveChanges(ActionEvent event) throws IOException {
        // Validate the input
        if (!validateInput()) {
            showAlert("Validation Error", "All fields must be filled.", Alert.AlertType.WARNING);
            return;
        }

        // Update the user object with the input data
        loggedInUser.setName(nameField.getText());
        loggedInUser.setEmail(emailField.getText());
        loggedInUser.setPhoneNumber(phoneField.getText());
        loggedInUser.setAddress(addressField.getText());
        loggedInUser.setPassword(passwordField.getText());

        // Save the changes to the database
        if (userDAO.updateUser(loggedInUser)) {
            // Update the SessionManager to reflect the updated user name
            SessionManager.getInstance().setLoggedInUser(loggedInUser.getName());
            
            //Load user dashoard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
            BorderPane userDashboard = loader.load();
            Scene scene = new Scene(userDashboard);
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } else {
            showAlert("Error", "Failed to save changes.", Alert.AlertType.ERROR);
        }
    }


    private boolean validateInput() {
        return !nameField.getText().isEmpty()
            && !emailField.getText().isEmpty()
            && !phoneField.getText().isEmpty()
            && !addressField.getText().isEmpty()
            && !passwordField.getText().isEmpty();
    }
    
    public void handleBack() throws IOException
    {
    	 FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
         BorderPane userDashboard = loader.load();
         Scene scene = new Scene(userDashboard);
         Stage stage = (Stage) rootPane.getScene().getWindow();
         stage.setScene(scene);
         stage.show();

    	
    }
    

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

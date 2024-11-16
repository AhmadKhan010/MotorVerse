package controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import dao.UserDAO;
import models.User;

public class SignUp {

	@FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    @FXML
    private void handleSignUp(ActionEvent event) {
        // Get user input
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String role = "Buyer"; // Default role (or retrieve from a dropdown, if applicable)

        // Validate input
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }
        
        // Validate email and name
		if (!emailValidator(email)|| !nameValidator(name)) {
			return;
		}
		

        // Create a User object
        User newUser = new User(name, email, password, phone, address, role);

        // Insert the user into the database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.insertUser(newUser);

        if (success) {
            showAlert("Success", "User registered successfully!", Alert.AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Error", "Failed to register user. Try again.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        phoneField.clear();
        addressField.clear();
    }
    
	private boolean emailValidator(String email) {
		// Email validation logic
		//Check if email exists in the database
		UserDAO userDAO = new UserDAO();
		boolean exists = userDAO.checkEmail(email);
		if (exists) {
			showAlert("Error", "Email already exists!", Alert.AlertType.ERROR);
			return false;
		}
		return true;
	}
	
	private boolean nameValidator(String name) {
		// Name validation logic
		// Check if name exists in the database
		UserDAO userDAO = new UserDAO();
		boolean exists = userDAO.checkName(name);
		if (exists) {
			showAlert("Error", "Name already exists!", Alert.AlertType.ERROR);
			return false;
		}
		return true;
	}
	
	
	
}

package controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainController {

    private Stage stage;
    private Scene scene;

    @FXML
    private AnchorPane OpeningBack; // Your main scene layout

    @FXML
    public void initialize() {
        // Add a delay of 2 seconds, then switch to the login scene
        PauseTransition delay = new PauseTransition(Duration.seconds(20));
        delay.setOnFinished(event -> switchToLogin());
        delay.play();
    }

    private void switchToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml")); // Load the login.fxml file
            stage = (Stage) OpeningBack.getScene().getWindow(); // Get the current stage
            scene = new Scene(root); // Set the new scene
	        
            stage.setScene(scene); // Apply the new scene to the stage
            stage.setResizable(true);
            stage.show(); // Show the stage
            
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions (e.g., file not found)
        }
    }
}

package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Login {

    @FXML
    private AnchorPane rootPane; // The parent AnchorPane

    @FXML
    private Label loginLabel; // The "LOG IN" Label

    @FXML
    public void initialize() {
        // Bind font size dynamically to the height of the root pane
        rootPane.heightProperty().addListener((obs, oldHeight, newHeight) -> {
            double fontSize = newHeight.doubleValue() * 0.05; // Adjust multiplier as needed
            loginLabel.setStyle("-fx-font-size: " + fontSize + "px;");
        });
    }
}


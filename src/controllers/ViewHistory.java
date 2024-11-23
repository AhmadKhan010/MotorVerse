package controllers;

import dao.PurchaseAgreementDAO;
import dao.RentalAgreementDAO;
import dao.VehicleDAO;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.fxml.FXML;
import models.PurchaseAgreement;
import models.RentalAgreement;
import models.User;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.List;

public class ViewHistory {

    @FXML
    private VBox purchaseVbox;

    @FXML
    private VBox rentalVbox;
    private User user;

    public void initialize() throws SQLException {
    	user = SessionManager.getInstance().getCurrentUser();
        // Load the purchase history
        loadPurchases();

        // Load the rental history
        loadRentals();
    }

    private void loadPurchases() throws SQLException {
        // Get the list of purchase agreements from the database
    	
        PurchaseAgreementDAO purchaseAgreementDAO = new PurchaseAgreementDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        List<PurchaseAgreement> purchases = purchaseAgreementDAO.getPurchaseAgreements(user.getUserId());
        
        // Clear the VBox to avoid duplicate entries
        purchaseVbox.getChildren().clear();

        for (PurchaseAgreement purchase : purchases) {
            // Create a styled HBox for each purchase
            HBox purchaseBox = new HBox(10); // Spacing between elements
            purchaseBox.setStyle("-fx-padding: 10; -fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #FFD580;");
            purchaseBox.setPadding(new Insets(10));
            
            // Fetch vehicle name
            String vehicleName = vehicleDAO.getVehicleName(purchase.getVehicleId());

            // Create labels for purchase details
            Label nameLabel = new Label("Vehicle: " + vehicleName);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            
            Label priceLabel = new Label("Price: $" + purchase.getPurchasePrice());
            priceLabel.setFont(Font.font("Arial", 12));
            
            Label dateLabel = new Label("Date: " + purchase.getPurchaseDate());
            dateLabel.setFont(Font.font("Arial", 12));

            // Add the labels to the HBox
            purchaseBox.getChildren().addAll(nameLabel, priceLabel, dateLabel);

            // Add the HBox to the purchase VBox
            purchaseVbox.getChildren().add(purchaseBox);
        }

        // Add spacing between each purchase card
        purchaseVbox.setSpacing(15);
    }

    private void loadRentals() throws SQLException {
        // Get the list of rental agreements from the database
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
        VehicleDAO vehicleDAO = new VehicleDAO();
        List<RentalAgreement> rentals = rentalAgreementDAO.getRentalAgreements(user.getUserId(),"All");

        // Clear the VBox to avoid duplicate entries
        rentalVbox.getChildren().clear();

        for (RentalAgreement rental : rentals) {
            // Create a styled HBox for each rental
            HBox rentalBox = new HBox(10); // Spacing between elements
            rentalBox.setStyle("-fx-padding: 10; -fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #FFE0B3;");
            rentalBox.setPadding(new Insets(10));

            // Fetch vehicle name
            String vehicleName = vehicleDAO.getVehicleName(rental.getVehicleId());

            // Create labels for rental details
            Label nameLabel = new Label("Vehicle: " + vehicleName);
            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            
            Label rentalCostLabel = new Label("Rental Cost: $" + rental.getRentalCost());
            rentalCostLabel.setFont(Font.font("Arial", 12));
            
            Label durationLabel = new Label("Duration: " + rental.getRentalPeriod());
            durationLabel.setFont(Font.font("Arial", 12));
            
            Label insuranceLabel = new Label("Insurance: " + rental.getInsuranceType() + " ($" + rental.getPremium() + ")");
            insuranceLabel.setFont(Font.font("Arial", 12));
            
          
            Label returnStatus = new Label("Return Status: " + rental.getReturn_status());
            returnStatus.setFont(Font.font("Arial", 12));
            
            // Add the labels to the HBox
            rentalBox.getChildren().addAll(nameLabel, rentalCostLabel, durationLabel, insuranceLabel,returnStatus);

            // Add the HBox to the rental VBox
            rentalVbox.getChildren().add(rentalBox);
        }

        // Add spacing between each rental card
        rentalVbox.setSpacing(15);
    }
}

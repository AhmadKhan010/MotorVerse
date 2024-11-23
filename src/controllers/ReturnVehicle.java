package controllers;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.RentalAgreementDAO;
import dao.VehicleDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.RentalAgreement;
import models.User;
import utils.SessionManager;

public class ReturnVehicle {

	 	@FXML
	    private BorderPane rootPane;

	    @FXML
	    private VBox vehicleDisplayVbox;
	    
	    
	    
	    
		public void initialize() throws SQLException
		{
			loadVehicles();
		}

		    // Handle the return vehicle action
			private void handleReturnVehicle(HBox rentalBox,RentalAgreement rental) {
			    // Retrieve the associated RentalAgreement object
			    
	
			    // Log the rental ID
			    System.out.println("Returning vehicle with Rental ID: " + rental.getRentalId());
	
			    // Update the return status in the database
			    RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
			    rentalAgreementDAO.updateReturnStatus(rental.getRentalId());
	
			    // Show a dialog for damage assessment
			    showDamageDialog(rental, rentalBox);
			}
			
			private void showDamageDialog(RentalAgreement rental, HBox rentalBox) {
			    // Create a new dialog
			    Stage dialogStage = new Stage();
			    dialogStage.initModality(Modality.APPLICATION_MODAL);
			    dialogStage.setTitle("Return Vehicle");

			    // Create VBox for layout
			    VBox dialogVBox = new VBox(10);
			    dialogVBox.setPadding(new Insets(20));

			    // Add dropdown for damage severity
			    Label label = new Label("Select Damage Severity:");
			    ChoiceBox<String> damageDropdown = new ChoiceBox<>();
			    damageDropdown.getItems().addAll("None", "Minor", "Medium", "Major");
			    damageDropdown.setValue("None"); // Default value

			    // Add button to confirm
			    Button confirmButton = new Button("Confirm");
			    confirmButton.setStyle("-fx-background-color: orange; -fx-text-fill: white; -fx-font-weight: bold;");

			    // Handle the button click
			    confirmButton.setOnAction(event -> {
			        String selectedDamage = damageDropdown.getValue();
			        try {
						calculateDamageCost(selectedDamage, rental, rentalBox, dialogStage);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    });

			    // Add elements to the VBox
			    dialogVBox.getChildren().addAll(label, damageDropdown, confirmButton);

			    // Create a scene and show the dialog
			    Scene scene = new Scene(dialogVBox, 300, 200);
			    dialogStage.setScene(scene);
			    dialogStage.show();
			}

			private void calculateDamageCost(String damageSeverity, RentalAgreement rental, HBox rentalBox, Stage dialogStage) throws SQLException {
			    // Define damage costs
			    double damageCost = 0;
			    switch (damageSeverity) {
			        case "None":
			            damageCost = 0;
			            break;
			        case "Minor":
			            damageCost = 0.10 * rental.getRentalCost();
			            break;
			        case "Medium":
			            damageCost = 0.25 * rental.getRentalCost();
			            break;
			        case "Major":
			            damageCost = 0.50 * rental.getRentalCost();
			            break;
			    }

			    // Compare with insurance premium
			    double premium = rental.getPremium();
			    double additionalCharge = Math.max(0, damageCost - premium);

			    // Update the database (RentalAgreementDAO logic)
//			    RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
//			    rentalAgreementDAO.updateDamageDetails(rental.getRentalId(), damageSeverity, damageCost, additionalCharge);

			    // Show results to the user
			    String message = additionalCharge == 0
			        ? "Damage cost is covered by insurance. No additional charges."
			        : "Damage cost exceeds insurance coverage. Additional charge: $" + additionalCharge;

			    showAlert("Damage Assessment", message, Alert.AlertType.INFORMATION);
			    showAlert("Vehicle Returned", "Vehicle has been successfully returned.", Alert.AlertType.INFORMATION);

			    // Close the dialog
			    dialogStage.close();

			    // Remove the returned vehicle from the display
			    vehicleDisplayVbox.getChildren().remove(rentalBox);
			    loadVehicles();
			    
			}
			
			private void showAlert(String title, String message, Alert.AlertType type) {
			    Alert alert = new Alert(type);
			    alert.setTitle(title);
			    alert.setContentText(message);
			    alert.showAndWait();
			}
			
			private void loadVehicles() throws SQLException
			{
				User user  = SessionManager.getInstance().getCurrentUser();
				RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
				List<RentalAgreement> rentals = rentalAgreementDAO.getRentalAgreements(user.getUserId(),"Not Returned");
				VehicleDAO vehicleDAO = new VehicleDAO();
				
				 vehicleDisplayVbox.getChildren().clear();
				 if(rentals == null)
				 {
						Label noRentalsLabel = new Label("No rentals found for this user");
						noRentalsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
						vehicleDisplayVbox.getChildren().add(noRentalsLabel);
						return;
				 }
				 
				 

			        // Iterate over the rentals and display each in a styled format
			        for (RentalAgreement rental : rentals) {
			            // Create a horizontal box for each rental
			        	HBox rentalBox = new HBox(10); // Spacing between elements inside the HBox
			            rentalBox.setStyle("-fx-padding: 10; -fx-border-color: orange; -fx-border-width: 2; -fx-border-radius: 5; -fx-background-radius: 5; -fx-background-color: #FFD580;");

			            rentalBox.setUserData(rental);
			            
			            // Add some margin to the rentalBox using a wrapper VBox
			            VBox wrapper = new VBox();
			            wrapper.setStyle("-fx-padding: 10;"); // Padding inside each wrapper
			            wrapper.setSpacing(10); // Spacing between different rental displays

			            // Fetch vehicle name using VehicleDAO
			            String vehicleName = vehicleDAO.getVehicleName(rental.getVehicleId());

			            // Create a VBox for the labels (multi-line grouping)
			            VBox detailsBox = new VBox(5); // Spacing between vertical elements

			            // Create labels for the details
			            Label nameLabel = new Label( vehicleName);
			            nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			            
			            Label priceLabel = new Label("Rental Price: $" + rental.getRentalCost());
			            priceLabel.setFont(Font.font("Arial", 12));
			            
			            Label daysLabel = new Label("Duration: " + rental.getRentalPeriod());
			            daysLabel.setFont(Font.font("Arial", 12));
			            
			            Label insuranceLabel = new Label("Insurance: " + rental.getInsuranceType() + " ($" + rental.getPremium() + ")");
			            insuranceLabel.setFont(Font.font("Arial", 12));

			            // Add labels to the VBox
			            detailsBox.getChildren().addAll(nameLabel, priceLabel, daysLabel, insuranceLabel);

			            // Create a button for returning the vehicle
			            Button returnButton = new Button("Return Now");
			            returnButton.setStyle("-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;");
			            
			            // Add event handler to handle the return action
			            returnButton.setOnAction(event -> handleReturnVehicle(rentalBox, rental));

			            // Add the VBox and the button to the HBox
			            rentalBox.getChildren().addAll(detailsBox, returnButton);

			            // Wrap the rentalBox with some padding and margin
			            wrapper.getChildren().add(rentalBox);

			            // Add the wrapper to the main VBox
			            vehicleDisplayVbox.getChildren().add(wrapper);
			        }
			}
			
			public void handleBack(ActionEvent event) throws IOException {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
				Parent root = loader.load();
				Stage stage = (Stage) rootPane.getScene().getWindow();
				stage.setScene(new Scene(root));
				stage.setTitle("User Dashboard - ");
				stage.show();
				
			}
		
	
}

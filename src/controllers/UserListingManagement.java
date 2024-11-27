
package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Listing;
import models.Vehicle;
import models.VehicleListingDTO;
import utils.SessionManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.ListingDAO;
import dao.VehicleDAO;

public class UserListingManagement {

    @FXML
    private TextField inputDescription;

    @FXML
    private TextField inputMake;

    @FXML
    private TextField inputModel;

    @FXML
    private TextField inputPrice;

    @FXML
    private TextField inputYear;

    @FXML
    private ComboBox<VehicleListingDTO> listingsComboBox;

    @FXML
    private Button photoUpload;
    @FXML
    private BorderPane rootPane;
    
    private File uploadedImage; // Store the uploaded image file
    

    private ListingDAO listingDAO = new ListingDAO(); // DAO instance for database operations
    // DAO instance for database operations

    @FXML
    public void initialize() throws SQLException {
        // Populate ComboBox with listings for the current user
    	List<VehicleListingDTO> listings = listingDAO.getListingsForCurrentUser(
                SessionManager.getInstance().getCurrentUser().getUserId()
            );
    	if (listings.isEmpty()) {
    		            showAlert("No Listings Found", "You have not listed any vehicles yet.", Alert.AlertType.INFORMATION);
    	}
    	
            ObservableList<VehicleListingDTO> vehicleListings = FXCollections.observableArrayList(listings);
            listingsComboBox.setItems(vehicleListings);

            // Display model, year, and price in ComboBox
            listingsComboBox.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(VehicleListingDTO dto) {
                    return dto.getModel() + " (" + dto.getYear() + ") - $" + dto.getPrice();
                }

                @Override
                public VehicleListingDTO fromString(String string) {
                    return null; // Not needed
                }
            });

        // Populate fields when a listing is selected
        listingsComboBox.setOnAction(event -> populateFields());
    }

    private void populateFields() {
    	 VehicleListingDTO selectedListing = listingsComboBox.getValue();
         if (selectedListing != null) {
             inputMake.setText(selectedListing.getMake());
             inputModel.setText(selectedListing.getModel());
             inputYear.setText(String.valueOf(selectedListing.getYear()));
             inputPrice.setText(String.valueOf(selectedListing.getPrice()));
             inputDescription.setText(selectedListing.getDescription());
         }
    }

    @FXML
    void handlePhotoUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        uploadedImage = fileChooser.showOpenDialog(photoUpload.getScene().getWindow());
        if (uploadedImage != null) {
            System.out.println("Selected file: " + uploadedImage.getAbsolutePath());
        }
    }

    @FXML
    void saveChanges(ActionEvent event) throws IOException {
    	 VehicleListingDTO selectedListing = listingsComboBox.getValue();
         if (selectedListing != null) {
             // Update DTO with new values
             selectedListing.setMake(inputMake.getText());
             selectedListing.setModel(inputModel.getText());
             selectedListing.setYear(Integer.parseInt(inputYear.getText()));
             selectedListing.setPrice(Double.parseDouble(inputPrice.getText()));
             selectedListing.setDescription(inputDescription.getText());

             // Set new image path if an image was uploaded
             if (uploadedImage != null) {
                 selectedListing.setImagePath(uploadedImage.getAbsolutePath());
             }

             // Save updated listing to the database
             boolean success = listingDAO.updateVehicleListing(selectedListing);
             if (success) {
                 System.out.println("Listing updated successfully.");
                 showAlert("Success", "Listing updated successfully!", Alert.AlertType.INFORMATION);
                 switchToUserDashboard();
             } else {
                 System.out.println("Failed to update the listing.");
             }
         }
         else
         {
        	 showAlert("Error", "Please select a listing to update", Alert.AlertType.ERROR);
         }
    }
    
    
	public void switchToUserDashboard() throws IOException 
	{

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("User Dashboard - ");
		stage.show();
		
	}
	
	public void handleBack(ActionEvent event) throws IOException {
		switchToUserDashboard();
	}
    
    
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

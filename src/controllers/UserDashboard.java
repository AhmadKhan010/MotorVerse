package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.VehicleDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Vehicle;
import models.VehicleListingDTO;
import utils.SessionManager;
import javafx.scene.Node ;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;


public class UserDashboard {

	@FXML
	private Button buyCarButton;
	@FXML private Button sellCarButton;
	@FXML private Button rentCarButton;
	@FXML private Button historyButton;
	@FXML private Button applyButton;
	@FXML private Button clearFilterButton;
	@FXML private Label priceLabel;
	@FXML private Label priceText;
	@FXML private Slider priceSlider;
	@FXML private ComboBox<String> makeFilter;
	@FXML private ComboBox<String> modelFilter;
	@FXML private ComboBox<Integer> yearFilter;
	@FXML private VBox vehicleGrid;
	@FXML private Label welcomeLabel;
	@FXML private BorderPane rootPane;
	@FXML private Label yearLabel;
	@FXML private Label filterText;
	@FXML private ScrollPane userScrollPane;
	//@FXML private ComboBox<String> statusFilter;
	VehicleDAO VehicleDAO;
	private Stage stage;
	private String currentRole="User";
	
	public void initialize() throws SQLException {
		
		
        VehicleDAO = new VehicleDAO();
//        makeFilter.setDisable(true);
//        yearFilter.setDisable(true);
//        priceSlider.setDisable(true);
//        applyButton.setDisable(true);
//        clearFilterButton.setDisable(true);
        makeFilter.setVisible(false);
        modelFilter.setVisible(false);
        yearFilter.setVisible(false);
        priceSlider.setVisible(false);
        applyButton.setVisible(false);
        clearFilterButton.setVisible(false);
        priceLabel.setVisible(false);
        priceText.setVisible(false);
        vehicleGrid.setVisible(false);
        filterText.setVisible(false);
        
        
        
        String username = SessionManager.getInstance().getLoggedInUser();
        welcomeLabel.setText("Welcome, " + username + "!");
        
        // Sample filter options (can be expanded with dynamic values from DB)
        makeFilter.getItems().addAll("Toyota", "Honda", "Ford", "BMW", "Audi", "Mercedes");
        yearFilter.getItems().addAll(2017, 2018, 2019, 2020, 2021, 2022);

        // Set default filters (optional)
        //        makeFilter.setValue("Toyota");
        //        yearFilter.setValue(2021);
        
        // Initialize price slider with a range of values
       
        
        //Initialize Status Filter (Available,Rented,Sold)
        //statusFilter.getItems().addAll("Available","Rented","Sold");

        // Load the available vehicles based on filters
        //loadVehicles();
    }

	// Add this method to handle the Profile Management button click
	@FXML
	private void handleProfileManagement() {
	    try {
	        Parent root = FXMLLoader.load(getClass().getResource("/views/ProfileManagement.fxml"));
	        Stage stage = (Stage) rootPane.getScene().getWindow();
	        stage.setScene(new Scene(root));
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	        showAlert("Error", "Failed to load the Profile Management view.", Alert.AlertType.ERROR);
	    }
	}

	
	private void refreshHeaderName() {
	    String updatedUsername = SessionManager.getInstance().getLoggedInUser(); // Get the updated user name
	    if (updatedUsername != null) {
	        welcomeLabel.setText("Welcome, " + updatedUsername + "!");
	    } else {
	        showAlert("Error", "Failed to refresh user name. Please reload the application.", Alert.AlertType.ERROR);
	    }
	}
	
	@FXML
    private void handleLogout(ActionEvent event) throws IOException {
        SessionManager.getInstance().setLoggedInUser(null);
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

	
	  private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }

	
	public void handleBuyCar() throws FileNotFoundException {
		System.out.println("Buy Car Button Clicked");
		
		currentRole="Buyer";
        // Clear previous entries in the grid
        vehicleGrid.getChildren().clear();
        // Add the vehicles to the grid
        priceSlider.setMin(0);
        priceSlider.setMax(1000000);  // Max price for the slider (can adjust based on your data)
        priceSlider.setBlockIncrement(2000);  // Set the step size for slider
        priceSlider.setValue(100000);  // Set a default value
        userScrollPane.setVisible(true);
        loadVehicles("Selling");
        makeFilter.setVisible(true);
        yearFilter.setVisible(true);
        priceSlider.setVisible(true);
        applyButton.setVisible(true);
        clearFilterButton.setVisible(true);
        priceLabel.setVisible(true);
        priceText.setVisible(true);
        vehicleGrid.setVisible(true);
        
	}
	
	public void handleRentCar() throws FileNotFoundException {

		currentRole = "Renter";
		vehicleGrid.getChildren().clear();

		priceSlider.setMin(0);
	    priceSlider.setMax(2000);  // Max price for the slider (can adjust based on your data)
	    priceSlider.setBlockIncrement(10);  // Set the step size for slider
	    priceSlider.setValue(2000);
	        
	    loadVehicles("Renting");
	   
        priceSlider.setShowTickLabels(true);
        priceSlider.setShowTickMarks(true);
	    makeFilter.setVisible(true);
        yearFilter.setVisible(true);
        priceSlider.setVisible(true);
        applyButton.setVisible(true);
        clearFilterButton.setVisible(true);
        priceLabel.setVisible(true);
        priceText.setVisible(true);
        vehicleGrid.setVisible(true);
		
	}
	
	public void handleSellCar()
	{
		System.out.println("Sell Car Button Clicked");
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/SellCar.fxml")); 
			stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
			Scene scene = new Scene(root); // Set the new scene
			stage.setScene(scene); // Apply the new scene to the stage
			stage.setResizable(true);
			stage.show(); // Show the stage
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	
	
	// Updated Function to Create a Vehicle Card
	private HBox createVehicleCard(VehicleListingDTO dto) throws FileNotFoundException {
	    // Vehicle Image
	    ImageView vehicleImage = new ImageView();
	    vehicleImage.setFitWidth(300); // Set the width
	    vehicleImage.setFitHeight(200);
	    //vehicleImage.setPreserveRatio(true); // Maintain aspect ratio
	    vehicleImage.setSmooth(true); // Enable smooth scaling
	    
	    try {
	        // The image path is stored in the database or user input
	        File file = new File(dto.getImagePath()); // Create a File object with the path
	        	
	        // Check if the file exists
	        if (!file.exists()) {
	            System.err.println("Image file not found: " + dto.getImagePath());
	            String imagePath = "/resource/Motoverse Logo.png";
	            Image image = new Image(getClass().getResourceAsStream(imagePath));
	            vehicleImage.setImage(image);
	        }

	        // Load the image from the file system using FileInputStream
	        Image image = new Image(new FileInputStream(file));
	        vehicleImage.setImage(image); // Set the image in the ImageView

	    } catch (FileNotFoundException e) {
	        System.err.println("Error loading image: " + e.getMessage());
	        String imagePath = "/resource/Motoverse Logo.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            vehicleImage.setImage(image);
	    } catch (Exception e) {
	        System.err.println("Unexpected error loading image: " + e.getMessage());
	    }


	    // Labels for vehicle details
	    Label makeModelLabel = new Label(dto.getMake() + " " + dto.getModel());
	    makeModelLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: orange;");
	    
	    Label yearLabel = new Label("Year: " + dto.getYear());
	    Label priceLabel = new Label("Price: $" + dto.getPrice());
	    Label descriptionLabel = new Label("Description: " + dto.getDescription());
	    Label sellerLabel = new Label("Seller: " + dto.getSellerName());

	    // Styling for the Labels
	    yearLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");
	    priceLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");
	    descriptionLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");
	    sellerLabel.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");
	    
	    Button buyNowButton = new Button(dto.getListingType().equals("Selling") ? "Buy Now" : "Rent Now");
	    buyNowButton.setStyle(
	        "-fx-background-color: orange; " +
	        "-fx-text-fill: white; " +
	        "-fx-font-size: 14px; " +
	        "-fx-font-weight: bold; " +
	        "-fx-background-radius: 5; " +
	        "-fx-cursor: hand;"
	    );
	    buyNowButton.setPrefWidth(150);
	    buyNowButton.setOnAction(e -> handleBuyNowButtonClick(dto)); 

	    // Container for the image and labels
	    VBox contentBox = new VBox(5);
	    contentBox.getChildren().addAll(makeModelLabel, yearLabel, priceLabel, descriptionLabel, sellerLabel, buyNowButton);
	    contentBox.setAlignment(Pos.CENTER);
	    contentBox.setPadding(new Insets(0,0,0,20));

	    // Card Layout
	    HBox cardLayout = new HBox(10); // Spacing between image and details
	    cardLayout.getChildren().addAll(vehicleImage, contentBox);
	    cardLayout.setAlignment(Pos.CENTER_LEFT); // Center the content within the card
	    cardLayout.setPadding(new Insets(15)); // Padding inside the card
	    cardLayout.setStyle(
	        "-fx-border-color: orange; " +
	        "-fx-background-color: #fcf6f6; " +
	        "-fx-border-radius: 10; " +
	        "-fx-background-radius: 10; " +
	        "-fx-effect: dropshadow(gaussian, #aaaaaa, 10, 0, 2, 2);"
	    );
	    cardLayout.setPrefHeight(200);
	    return cardLayout;
	}

	public void applyFilters(ActionEvent event) throws FileNotFoundException
	{
		loadVehicles(currentRole=="Buyer"?"Selling":"Renting");
		handlePriceDrag();
	}

	public void clearFilters(ActionEvent event) throws FileNotFoundException {
		makeFilter.setValue(null);
		yearFilter.setValue(null);
		priceSlider.setValue(100000);
		loadVehicles(currentRole=="Buyer"?"Selling":"Renting");
		handlePriceDrag();
	}
	
	private void loadVehicles(String listingType) throws FileNotFoundException {
	    String make = makeFilter.getValue();
	    Integer year = yearFilter.getValue();
	    Double minPrice = 0.0;
	    Double maxPrice = priceSlider.getValue();

	    // Fetch all available vehicles based on the filters
	    List<VehicleListingDTO> vehicleListings = dao.VehicleDAO.getAvailableVehicles(listingType,make, year, minPrice, maxPrice);
	    
	    // Clear previous entries in the GridPane
	    vehicleGrid.getChildren().clear();
	    vehicleGrid.setPadding(new Insets(10, 20, 10, 20));
	    vehicleGrid.setSpacing(20);

	    
	    for (VehicleListingDTO dto : vehicleListings) {
	        HBox vehicleCard = createVehicleCard(dto);
	        //Add to Vbox vehicleGrid
	        vehicleGrid.getChildren().add(vehicleCard);

	    }
	}
	
	public void handleBuyNowButtonClick(VehicleListingDTO dto) {
		try {
			Parent root = null;
			FXMLLoader loader = null;

			if (dto.getListingType().equals("Renting")) {
			    loader = new FXMLLoader(getClass().getResource("/views/RentVehicle.fxml"));
			    root = loader.load();
			    
			    // Get controller and pass the DTO
			    RentVehicle controller = loader.getController();
			    controller.setDto(dto);
			} else {
			    loader = new FXMLLoader(getClass().getResource("/views/BuyVehicle.fxml"));
			    root = loader.load();
			    
			    // Get controller and pass the DTO
			    BuyVehicle controller = loader.getController();
			    controller.setDto(dto);
			}

			// Open the new scene
			Stage stage = (Stage) rootPane.getScene().getWindow();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Buy Vehicle - " + dto.getMake() + " " + dto.getModel());
			stage.show();

	    } catch (IOException e) {
	        System.err.println("Error loading BuyVehicle.fxml: " + e.getMessage()	);
	        e.printStackTrace();
	        e.getCause();
	    }
	}
	
	
	public void returnVehicle()
	{
		try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/ReturnVehicle.fxml"));
            stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
            Scene scene = new Scene(root); // Set the new scene
            scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
            stage.setScene(scene); // Apply the new scene to the stage
            stage.setResizable(true);
            stage.show(); // Show the stage
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
	
	
	public void viewHistory()
	{
		try {
			 Parent root = FXMLLoader.load(getClass().getResource("/views/ViewHistory.fxml"));

		        // Create a new stage for the ViewHistory page
		        Stage newStage = new Stage();
		        newStage.setTitle("View History");

		        // Set the scene for the new stage
		        Scene scene = new Scene(root);
		        scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		        newStage.setScene(scene);

		        // Optional: Set stage properties
		        newStage.setResizable(true);

		        // Show the new stage
		        newStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void buyAutoParts()
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/AutoParts.fxml"));
			stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
			Scene scene = new Scene(root); // Set the new scene
			//scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene); // Apply the new scene to the stage
			stage.setResizable(true);
			stage.show(); // Show the stage
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void handleListingManagement()
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/ListingManagement.fxml"));
			stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
			Scene scene = new Scene(root); // Set the new scene
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene); // Apply the new scene to the stage
			stage.setResizable(true);
			stage.show(); // Show the stage
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}
	
	public void handleCustomerSupport()
	{
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/views/CustomerSupport.fxml"));
			stage = (Stage) rootPane.getScene().getWindow(); // Get the current stage
			Scene scene = new Scene(root); // Set the new scene
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene); // Apply the new scene to the stage
			stage.setResizable(true);
			stage.show(); // Show the stage
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	public void handlePriceDrag()
	{
		 int price = (int) priceSlider.getValue(); 
	        priceLabel.setText(String.valueOf(price));
		
	}
	
	
	
	
}

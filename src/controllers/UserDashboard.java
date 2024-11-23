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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Vehicle;
import models.VehicleListingDTO;
import utils.SessionManager;

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
	@FXML private GridPane vehicleGrid;
	@FXML private Label welcomeLabel;
	@FXML private BorderPane rootPane;
	@FXML private Label yearLabel;
	@FXML private Label filterText;
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

	
	
	public void handleBuyCar() {
		System.out.println("Buy Car Button Clicked");
		
		currentRole="Buyer";
        // Clear previous entries in the grid
        vehicleGrid.getChildren().clear();
        // Add the vehicles to the grid
        priceSlider.setMin(0);
        priceSlider.setMax(100000);  // Max price for the slider (can adjust based on your data)
        priceSlider.setBlockIncrement(1000);  // Set the step size for slider
        priceSlider.setValue(30000);  // Set a default value
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
	
	public void handleRentCar() {

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
   	private VBox createVehicleCard(VehicleListingDTO dto) {
	    // Vehicle Image
	    ImageView vehicleImage = new ImageView();
	    vehicleImage.setFitWidth(200); // Set the width
	    vehicleImage.setPreserveRatio(true); // Maintain aspect ratio
	    vehicleImage.setSmooth(true); // Enable smooth scaling
	    
	    // Load the image from the file path
	    try {
	        String imagePath = "/resource/Motoverse Logo.png"; // Image folder and filename
	        Image image = new Image(getClass().getResourceAsStream(imagePath));
	        vehicleImage.setImage(image);
	    } catch (Exception e) {
	        System.err.println("Error loading image for " + dto.getMake() + " " + dto.getModel() + ": " + e.getMessage());
	        // Default image in case of error
	        vehicleImage.setImage(new Image(getClass().getResourceAsStream("/resource/Motoverse Logo.png")));
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

	    // Card Layout
	    VBox cardLayout = new VBox(10); // Spacing between image and details
	    cardLayout.getChildren().addAll(vehicleImage, contentBox);
	    cardLayout.setAlignment(Pos.TOP_CENTER); // Center the content within the card
	    cardLayout.setPadding(new Insets(15)); // Padding inside the card
	    cardLayout.setStyle(
	        "-fx-border-color: orange; " +
	        "-fx-background-color: #fdf3e7; " +
	        "-fx-border-radius: 10; " +
	        "-fx-background-radius: 10; " +
	        "-fx-effect: dropshadow(gaussian, #aaaaaa, 10, 0, 2, 2);"
	    );

	    return cardLayout;
	}

	public void applyFilters(ActionEvent event)
	{
		loadVehicles(currentRole=="Buyer"?"Selling":"Renting");
		handlePriceDrag();
	}

	public void clearFilters(ActionEvent event) {
		makeFilter.setValue(null);
		yearFilter.setValue(null);
		priceSlider.setValue(100000);
		loadVehicles(currentRole=="Buyer"?"Selling":"Renting");
		handlePriceDrag();
	}
	
	private void loadVehicles(String listingType) {
	    String make = makeFilter.getValue();
	    Integer year = yearFilter.getValue();
	    Double minPrice = 0.0;
	    Double maxPrice = priceSlider.getValue();

	    // Fetch all available vehicles based on the filters
	    List<VehicleListingDTO> vehicleListings = dao.VehicleDAO.getAvailableVehicles(listingType,make, year, minPrice, maxPrice);
	    
	    // Clear previous entries in the GridPane
	    vehicleGrid.getChildren().clear();
	    vehicleGrid.setPadding(new Insets(20));

	    // Add the vehicles to the GridPane
	    int row = 0;
	    int col = 0;

	    for (VehicleListingDTO dto : vehicleListings) {
	        VBox vehicleCard = createVehicleCard(dto);
	        vehicleGrid.add(vehicleCard, col, row); // Add to GridPane at (col, row)

	        col++;  // Move to the next column
	        if (col >= 1) {  // After 3 items, move to the next row
	            col = 0;
	            row++;
	        }
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
	
	
	
	
	public void handlePriceDrag()
	{
		 int price = (int) priceSlider.getValue(); 
	        priceLabel.setText(String.valueOf(price));
		
	}
	
	
	
	
}

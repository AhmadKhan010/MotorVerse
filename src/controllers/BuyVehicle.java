package controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import dao.PaymentDAO;
import dao.PurchaseAgreementDAO;
import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Payment;
import models.PurchaseAgreement;
import models.User;
import models.VehicleListingDTO;
import services.Invoice;
import utils.SessionManager;

public class BuyVehicle {

    @FXML
    private RadioButton bankTransferToggel;

    @FXML
    private Button buyButton;

    @FXML
    private Label buyingDescriptionLabel;

    @FXML
    private Label buyingMakeLabel;

    @FXML
    private Label buyingModelLabel;

    @FXML
    private Label buyingPriceLabel,purchaseCostInvoice,insurancePackage,insurancePackageCost,premiumInvoice,totalInvoice;

    @FXML BorderPane rootPane;
    	
    @FXML
    private Label buyingRatingLabel,totalLabel,totalLabel2;

    @FXML
    private Button cancelButtin;

    @FXML
    private ToggleGroup choosePaymentMethod;

    @FXML
    private AnchorPane creditCardTab;

    @FXML
    private RadioButton creditCardToggel;

    @FXML
    private AnchorPane debitCardTab;

    @FXML
    private AnchorPane debitCardTab1;

    @FXML
    private RadioButton debitCardToggel;

    @FXML
    private ComboBox<String> insuranceOptionBox;

    @FXML
    private AnchorPane payPalTab, invoicePane;

    @FXML
    private RadioButton paypalToggel;

    @FXML
    private Button submitButton;

    @FXML
    private Button testDriveButton;
    @FXML ImageView vehicleImage; 
    @FXML AnchorPane paymentAnchor;
    
    @FXML private TextField nameField, cardNumber, cvv, emailField, passwordField;
    @FXML private DatePicker expiryDate;
    
    VehicleListingDTO vehicle;
    Invoice invoice;
    double total,premium;
    String insuranceType;
    
    
	public void setDto(VehicleListingDTO vehicle) {
		this.vehicle = vehicle;
		buyingMakeLabel.setText(vehicle.getMake());
		buyingModelLabel.setText(vehicle.getModel());
		buyingDescriptionLabel.setText(vehicle.getDescription());
		buyingPriceLabel.setText("$" + vehicle.getPrice());
		buyingRatingLabel.setText(String.valueOf(vehicle.getAverageRating()));
	}

	
	public void initialize()
	{
		insuranceOptionBox.getItems().addAll("None", "Collision", "PIP", "Engine Protection");
        insuranceOptionBox.setValue("None");
        
        
        paymentAnchor.setVisible(false);
        
        vehicleImage.setFitWidth(579);
        vehicleImage.setFitHeight(309);
        vehicleImage.setPreserveRatio(false);
        vehicleImage.setSmooth(true);
        vehicleImage.setCache(true);

        try {
            String imagePath = "/resource/Motoverse Logo.png";
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            vehicleImage.setImage(image);
        } catch (Exception e) {
            System.out.println("Image not found");
        }
        
        
    }
	public void handleBuyNow(ActionEvent event) {
        insuranceType = insuranceOptionBox.getValue();
       
        double rentalPrice = vehicle.getPrice();
        premium = insuranceType.equals("None") ? 0.0 : utils.PremiumCalculator.calculatePremium(rentalPrice, insuranceType);

        total = rentalPrice  + premium;
        totalLabel.setText("$" + total); totalLabel2.setText("$" + total);
        invoice = new Invoice(0,total, 0, insuranceType, vehicle);
        
        handlePayment();
    }

    public void handlePayment() {
        paymentAnchor.setVisible(true);
        creditCardToggel.setSelected(true); // Default to credit card payment
        togglePaymentMethod(); // Ensure the correct tab is displayed
    }

    public void togglePaymentMethod() {
    	boolean isCreditCard;
    	try
        {
    		 isCreditCard = creditCardToggel.isSelected();
    		 //creditCardToggle.setSelected(isCreditCard);
        }
    	catch (Exception e)
    	{
    		 isCreditCard = false;
    		 System.out.println("Error: " + e);
    	}
    	creditCardTab.setVisible(isCreditCard);
        payPalTab.setVisible(!isCreditCard);
    }

    public void handleConfirmCreditCard(ActionEvent event) throws SQLException, IOException {
        String name = nameField.getText().trim();
        String cardNum = cardNumber.getText().trim();
        String cvvCode = cvv.getText().trim();
        LocalDate expiry = expiryDate.getValue();

        if (name.isEmpty() || cardNum.isEmpty() || cvvCode.isEmpty() || expiry == null) {
            showAlert("Missing Information", "All fields must be filled out", Alert.AlertType.ERROR);
            return;
        }

        if (!cardNum.matches("\\d{16}")) {
            showAlert("Invalid Card Number", "Card number must be 16 digits", Alert.AlertType.ERROR);
            return;
        }

        if (!cvvCode.matches("\\d{3}")) {
            showAlert("Invalid CVV", "CVV must be 3 digits", Alert.AlertType.ERROR);
            return;
        }

        if (expiry.isBefore(LocalDate.now())) {
            showAlert("Invalid Expiry Date", "Expiry date must be in the future", Alert.AlertType.ERROR);
            return;
        }
        User user = SessionManager.getInstance().getCurrentUser();
        int buyerId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        
        double price = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
       
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement(buyerId, user.getName(), vehicleId, LocalDate.now().toString(), price, insuranceOptionBox.getValue(), totalPremium, sellerId);
        PurchaseAgreementDAO purchaseAgreementDAO = new PurchaseAgreementDAO(purchaseAgreement);
        purchaseAgreementDAO.insertPurchaseAgreement();
        
        Payment payment = new Payment(buyerId,price,"Credit Card");
        PaymentDAO.insertPayment(payment);
        
        invoicePane.setVisible(true);
        purchaseCostInvoice.setText("$"+vehicle.getPrice());
        insurancePackageCost.setText("$"+premium);
        insurancePackage.setText(insuranceType);
        premiumInvoice.setText("$"+totalPremium);
        totalInvoice.setText("$"+total);
        
        showAlert("Processing Payment","Processing Payment with amount $"+total+" for "+user.getName(), Alert.AlertType.INFORMATION);
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        paymentAnchor.setVisible(false); // Hide payment pane
        endBuy();
    }

    public void handleConfirmPayPal(ActionEvent event) throws SQLException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Missing Information", "Email and password are required", Alert.AlertType.ERROR);
            return;
        }

        if (!email.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert("Invalid Email", "Please enter a valid email address", Alert.AlertType.ERROR);
            return;
        }

        if (password.length() < 8) {
            showAlert("Weak Password", "Password must be at least 8 characters", Alert.AlertType.ERROR);
            return;
        }

        User user = SessionManager.getInstance().getCurrentUser();
        int buyerId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        
        double price = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
       
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement(buyerId, user.getName(), vehicleId, LocalDate.now().toString(), price, insuranceOptionBox.getValue(), totalPremium, sellerId);
        PurchaseAgreementDAO purchaseAgreementDAO = new PurchaseAgreementDAO(purchaseAgreement);
        purchaseAgreementDAO.insertPurchaseAgreement();
        
        Payment payment = new Payment(buyerId,price,"PayPal");
        PaymentDAO.insertPayment(payment);
        
        invoicePane.setVisible(true);
        purchaseCostInvoice.setText("$"+vehicle.getPrice());
        insurancePackageCost.setText("$"+premium);
        insurancePackage.setText(insuranceType);
        premiumInvoice.setText("$"+totalPremium);
        totalInvoice.setText("$"+total);
       
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        paymentAnchor.setVisible(false); // Hide payment pane
    }
    
    public void endBuy() throws IOException {
		invoicePane.setVisible(false);
		//Open UserDashboard:
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) rootPane.getScene().getWindow();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("User Dashboard - ");
		stage.show();
	    
	}

    public void handleCancel(ActionEvent event) {
        paymentAnchor.setVisible(false); // Hide payment pane
    }

	
	 private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	
	
	
}

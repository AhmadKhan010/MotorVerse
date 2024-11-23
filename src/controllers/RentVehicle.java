package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.Payment;
import models.RentalAgreement;
import models.User;
import models.VehicleListingDTO;
import services.Invoice;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

import dao.PaymentDAO;
import dao.RentalAgreementDAO;
import dao.UserDAO;

public class RentVehicle {

    @FXML
    private Label makeLabel, modelLabel, rentalPriceLabel, ratingLabel, totalLabel,totalLabel2,rentalCostInvoice,rentDaysInvoice,insurancePackage,insurancePackageCost,premiumInvoice,totalInvoice;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ComboBox<String> insuranceOptionBox;

    @FXML
    private TextField rentDays, nameField, cardNumber, cvv, emailField, passwordField;

    @FXML
    private DatePicker expiryDate;

    @FXML
    private ImageView vehicleImage, starPicture;

    @FXML
    private AnchorPane paymentAnchor, creditCardTab, payPalTab,invoicePane;
    
    @FXML BorderPane rootPane;
    
    @FXML
    private RadioButton creditCardToggle, payPalToggle;

    private VehicleListingDTO vehicle;
    private Invoice invoice;
    int days;
    double total,premium;
    String insuranceType;
    
    
    public void setDto(VehicleListingDTO vehicle) {
        this.vehicle = vehicle;
        makeLabel.setText(vehicle.getMake());
        modelLabel.setText(vehicle.getModel());
        descriptionArea.setText(vehicle.getDescription());
        rentalPriceLabel.setText("$" + vehicle.getPrice() + " per day");
        ratingLabel.setText(String.valueOf(vehicle.getAverageRating()));
    }

    public void initialize() {
        insuranceOptionBox.getItems().addAll("None", "Collision", "PIP", "Engine Protection");
        insuranceOptionBox.setValue("None");
        paymentAnchor.setVisible(false);
        

        vehicleImage.setFitWidth(387);
        vehicleImage.setFitHeight(245);
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

        starPicture.setPreserveRatio(true);
        starPicture.setSmooth(true);
        starPicture.setCache(true);
        try {
            String starImagePath = "/resource/yellow_star.png";
            Image starImageResource = new Image(getClass().getResource(starImagePath).toExternalForm());
            starPicture.setImage(starImageResource);
        } catch (Exception e) {
            System.out.println("Star image not found");
        }

        // Toggle payment method panes based on radio button selection
        //creditCardToggle.setOnAction(e -> togglePaymentMethod());
        //payPalToggle.setOnAction(e -> togglePaymentMethod());
    }

    public void handleRentNow(ActionEvent event) {
        insuranceType = insuranceOptionBox.getValue();
        
        try {
            days = Integer.parseInt(rentDays.getText());
        } catch (Exception e) {
            showAlert("Invalid Input", "Please enter a valid number of days", Alert.AlertType.ERROR);
            return;
        }

        double rentalPrice = vehicle.getPrice();
        premium = insuranceType.equals("None") ? 0.0 : utils.PremiumCalculator.calculatePremium(rentalPrice, days, insuranceType);

        total = rentalPrice * days + premium;
        totalLabel.setText("$" + total); totalLabel2.setText("$" + total);
        invoice = new Invoice(rentalPrice,0, days, insuranceType, vehicle);               

        handlePayment();
    }

    public void handlePayment() {
        paymentAnchor.setVisible(true);
        creditCardToggle.setSelected(true); // Default to credit card payment
        togglePaymentMethod(); // Ensure the correct tab is displayed
    }

    public void togglePaymentMethod() {
    	boolean isCreditCard;
    	try
        {
    		 isCreditCard = creditCardToggle.isSelected();
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

    public void handleConfirmCreditCard(ActionEvent event) throws SQLException {
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
        int renterId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        String rentalPeriod = days + " days";
        double rentalCostTotal = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
        
        RentalAgreement rentalAgreement = new RentalAgreement(renterId, vehicleId, rentalPeriod, rentalCostTotal, insuranceType, totalPremium, sellerId);
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO(rentalAgreement);
        rentalAgreementDAO.insertRentalAgreement();
        
        Payment payment = new Payment(user.getUserId(), total, "Credit Card");
        PaymentDAO.insertPayment(payment);
        
        
        
        invoicePane.setVisible(true);
        rentalCostInvoice.setText("$"+vehicle.getPrice());
        insurancePackageCost.setText("$"+premium);
        rentDaysInvoice.setText(days+" days");
        insurancePackage.setText(insuranceType);
        premiumInvoice.setText("$"+totalPremium);
        totalInvoice.setText("$"+total);
       
        paymentAnchor.setVisible(false); // Hide payment pane
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        
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
        int renterId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        String rentalPeriod = days + " days";
        double rentalCost = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
        
        RentalAgreement rentalAgreement = new RentalAgreement(renterId, vehicleId, rentalPeriod, rentalCost, insuranceType, totalPremium, sellerId);
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO(rentalAgreement);
        rentalAgreementDAO.insertRentalAgreement();
        
        Payment payment = new Payment(user.getUserId(), total, "PayPal");
        PaymentDAO.insertPayment(payment);
        
        invoicePane.setVisible(true);
        rentalCostInvoice.setText("$"+vehicle.getPrice());
        insurancePackageCost.setText("$"+premium);
        rentDaysInvoice.setText(days+" days");
        insurancePackage.setText(insuranceType);
        premiumInvoice.setText("$"+totalPremium);
        totalInvoice.setText("$"+total);
        
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        paymentAnchor.setVisible(false); // Hide payment pane
    }

    public void handleCancel(ActionEvent event) {
        paymentAnchor.setVisible(false); // Hide payment pane
    }
    
	public void endRent(ActionEvent event) throws IOException {
		invoicePane.setVisible(false);
		//Open UserDashboard:
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RentVehicle.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("User Dashboard - ");
		stage.show();
	    
	}
    
	public void handleBack() throws IOException
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setTitle("User Dashboard - ");
		stage.show();
		
		
	}
    

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

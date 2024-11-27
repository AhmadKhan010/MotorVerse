package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import dao.PaymentDAO;
import dao.PurchaseAgreementDAO;
import dao.UserDAO;
import dao.VehicleDAO;
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
import javafx.scene.control.TextArea;
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
import services.CreditCardPayment;
import services.Invoice;
import services.PayPalPayment;
import services.PaymentContext;
import services.PaymentProcessor;
import utils.SessionManager;

public class BuyVehicle {

    @FXML
    private RadioButton bankTransferToggel;

    @FXML
    private Button buyButton;

    @FXML
    private TextArea buyingDescriptionLabel;

    @FXML
    private Label buyingMakeLabel;

    @FXML
    private Label buyingModelLabel,carName;

    @FXML
    private Label buyingPriceLabel,purchaseCostInvoice,insurancePackage,insurancePackageCost,premiumInvoice,totalInvoice;

    @FXML BorderPane rootPane;
    	
    @FXML
    private Label buyingRatingLabel,totalLabel,totalLabel2,buyingYear;

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
    
    
	public void setDto(VehicleListingDTO vehicle) throws FileNotFoundException {
		this.vehicle = vehicle;
		buyingMakeLabel.setText(vehicle.getMake());
		buyingModelLabel.setText(vehicle.getModel());
		buyingDescriptionLabel.setText(vehicle.getDescription());
		buyingPriceLabel.setText("$" + vehicle.getPrice());
		buyingRatingLabel.setText(String.valueOf(vehicle.getAverageRating()));
		buyingYear.setText(String.valueOf(vehicle.getYear()));
		
		 vehicleImage.setFitWidth(579);
	        vehicleImage.setFitHeight(309);
	        vehicleImage.setPreserveRatio(false);
	        vehicleImage.setSmooth(true);
	        vehicleImage.setCache(true);
	        
	        String imagePath = vehicle.getImagePath();
	        File file = new File(imagePath);
	        if(file.exists())
	        {
	        	Image image = new Image(new FileInputStream(file));
	        	vehicleImage.setImage(image);
	        }
	        else
	        {
	            imagePath = "/resource/Motoverse Logo.png";
	            Image image = new Image(getClass().getResourceAsStream(imagePath));
	            vehicleImage.setImage(image);
	        }
	}

	
	public void initialize() 
	{
		insuranceOptionBox.getItems().addAll("None", "Collision", "PIP", "Engine Protection");
        insuranceOptionBox.setValue("None");
        
        
        paymentAnchor.setVisible(false);
        
       
       
        
        
    }
	public void handleBuyNow(ActionEvent event) {
        insuranceType = insuranceOptionBox.getValue();
       
        double rentalPrice = vehicle.getPrice();
        premium = insuranceType.equals("None") ? 0.0 : utils.PremiumCalculator.calculatePremium(rentalPrice, insuranceType);

        total = rentalPrice  + premium;
        totalLabel.setText("$" + total); 
        invoice = new Invoice(0,total, 0, insuranceType, vehicle);
        
       
        invoicePane.setVisible(true);
        carName.setText(vehicle.getMake() + " " + vehicle.getModel());
        purchaseCostInvoice.setText("$"+vehicle.getPrice());
        insurancePackageCost.setText("$"+utils.PremiumCalculator.getInsuranceRate(insuranceType));
        insurancePackage.setText(insuranceType);
        premiumInvoice.setText("$"+premium);
        totalInvoice.setText("$"+total);
        
        
        
    }
	
	public void handlePayNow()
	{
		handlePayment();
	}
	
	

    public void handlePayment() {
    	invoicePane.setVisible(false);
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

        PaymentContext paymentContext = new PaymentContext();
        PaymentProcessor paymentProcessor = null;
        paymentProcessor = new CreditCardPayment(name, cardNum, expiry, cvvCode);
        
        paymentContext.setPaymentProcessor(paymentProcessor);
        boolean paymentSuccessful =  paymentContext.executePayment(total);
        
        User user = SessionManager.getInstance().getCurrentUser();
        int buyerId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        
        double price = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
        
        if(paymentSuccessful)
        {
       
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement(buyerId, user.getName(), vehicleId, LocalDate.now().toString(), price, insuranceOptionBox.getValue(), totalPremium, sellerId);
        PurchaseAgreementDAO purchaseAgreementDAO = new PurchaseAgreementDAO(purchaseAgreement);
        purchaseAgreementDAO.insertPurchaseAgreement();
        
        Payment payment = new Payment(buyerId,price,"Credit Card");
        PaymentDAO.insertPayment(payment);
        
        VehicleDAO vehicleDAO = new VehicleDAO();
        vehicleDAO.updateVehicleStatus(vehicleId, "Sold");
       
        
       // showAlert("Processing Payment","Processing Payment with amount $"+total+" for "+user.getName(), Alert.AlertType.INFORMATION);
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        paymentAnchor.setVisible(false); // Hide payment pane
        endBuy();
        }
        
        
    }

    public void handleConfirmPayPal(ActionEvent event) throws SQLException, IOException {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        PaymentContext paymentContext = new PaymentContext();
        PaymentProcessor paymentProcessor = null;
        paymentProcessor = new PayPalPayment(email, password);

        paymentContext.setPaymentProcessor(paymentProcessor);
        boolean paymentSuccessful = paymentContext.executePayment(total);
        
        User user = SessionManager.getInstance().getCurrentUser();
        int buyerId =  user.getUserId();
        int vehicleId = vehicle.getVehicleId();
        
        double price = total;
        double totalPremium = premium;
        UserDAO userDAO = new UserDAO();
        User seller = userDAO.getUser(vehicle.getSellerName());
        int sellerId = seller.getUserId();
       
        if(paymentSuccessful)
        {
        PurchaseAgreement purchaseAgreement = new PurchaseAgreement(buyerId, user.getName(), vehicleId, LocalDate.now().toString(), price, insuranceOptionBox.getValue(), totalPremium, sellerId);
        PurchaseAgreementDAO purchaseAgreementDAO = new PurchaseAgreementDAO(purchaseAgreement);
        purchaseAgreementDAO.insertPurchaseAgreement();
        
        Payment payment = new Payment(buyerId,price,"PayPal");
        PaymentDAO.insertPayment(payment);
        
        VehicleDAO vehicleDAO = new VehicleDAO();
        vehicleDAO.updateVehicleStatus(vehicleId, "Sold");
       
        showAlert("Payment Successful", "Thank you for your payment!", Alert.AlertType.INFORMATION);
        paymentAnchor.setVisible(false); // Hide payment pane
        endBuy();
        }
		else {
			
		}
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
    
    
    public void testDrive()
    {
		// Open TestDrive:
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TestDrive.fxml"));
		Parent root;
		try {
			root = loader.load();
			TestDriveController controller = loader.getController();
			controller.setVehicleId(vehicle.getVehicleId());
			controller.setVehicleName(vehicle.getMake() + " " + vehicle.getModel());
			
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Test Drive - ");
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

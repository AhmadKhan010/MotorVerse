package controllers;

import java.io.IOException;
import java.sql.SQLException;

import dao.AutoPartPurchaseDAO;
import dao.PaymentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.AutoPartsPurchase;
import models.Payment;
import models.User;
import services.CreditCardPayment;
import services.PayPalPayment;
import services.PaymentContext;
import services.PaymentProcessor;
import utils.SessionManager;

public class AutoParts {

    @FXML
    private Label available1;
    @FXML private Label available2;

    @FXML
    private Label available3;
    @FXML
    private Label available4;

    @FXML
    private Button backButton;

    @FXML
    private RadioButton bankTransferToggel;

    @FXML
    private Button buy1Button;

    @FXML
    private Button buy2Button;

    @FXML
    private Button buy3Button;

    @FXML
    private Button buy4Button;

    @FXML
    private Button cancelPayment;

    @FXML
    private ToggleGroup choosePaymentMethod;

    @FXML
    private Label clutchplateCost;

    @FXML
    private Label clutchplateLabel;

    @FXML
    private Label clutchplateQuantity;

    @FXML
    private AnchorPane creditCardTab;

    @FXML
    private RadioButton creditCardToggel;

    @FXML
    private RadioButton debitCardToggel;

    @FXML
    private Label engineOilCost;

    @FXML
    private Label engineOilLabel;

    @FXML
    private Label engineOilQuantity;

    @FXML
    private Label fuelpumpCost;

    @FXML
    private Label fuelpumpLabel;

    @FXML
    private Label fuelpumpQuantity;

    @FXML
    private Label headlightsCost;

    @FXML
    private Label headlightsLabel;

    @FXML
    private Label headligthsQuantity, totalLabel;

    @FXML
    private Button logInPayPal;

    @FXML
    private Label partDetail1;

    @FXML
    private Label partDetail2;

    @FXML
    private Label partDetail3;

    @FXML
    private Button payButton;

    @FXML
    private AnchorPane payPalTab,paymentAnchor;
    @FXML
    private AnchorPane carpartInvoice;
    @FXML
    private RadioButton paypalToggel;

    @FXML
    private TextField qtyField1;

    @FXML
    private TextField qtyField2;

    @FXML
    private TextField qtyField3;

    @FXML
    private TextField qtyField4;

    @FXML
    private Button searchButton,checkoutButton;

    @FXML
    private TextField searchField, nameField, cardNumber,cvv,emailField,passwordField;

    @FXML private DatePicker expiryDate;
    
    @FXML
    private Button submitPayment;

    @FXML
    private Label totalCost;

    @FXML private Button userDashboardButton,clearCartButton;; 
    @FXML private BorderPane rootPane;
     
    
    int quantity;
    double total;
    int partId;
    AutoPartsPurchase purchase1,purchase2,purchase3,purchase4;
    User user; int userId;
    AutoPartPurchaseDAO purchaseDAO;
    
    
    public void initialize() {
    	user = SessionManager.getInstance().getCurrentUser(); 
    	userId = user.getUserId();
    	purchase1 = new AutoPartsPurchase();
    	purchase2 = new AutoPartsPurchase();
    	purchase3 = new AutoPartsPurchase();
    	purchase4 = new AutoPartsPurchase();
    	
    	checkoutButton.setVisible(false);
    	clearCartButton.setVisible(false);
    	purchaseDAO = new AutoPartPurchaseDAO();
    	}
    

    @FXML
    void buyClutchPlate(ActionEvent event) {
    	int quantity = 0;
    	try {
    		 quantity = Integer.parseInt(qtyField4.getText());
    	}
    	catch (Exception e)
    	{
    		showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
    		return;
    	}
    	
		if (quantity <= 0) {
			showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
			return;
		}
		
		//Check if quantity is available
		if (!purchaseDAO.checkQuantity(9, quantity)) {
			showAlert("Error", "Not enough quantity available", Alert.AlertType.ERROR);
			return;
		}
		
		
		
		double total = quantity * 60;
		clutchplateCost.setText("$" + total);
		clutchplateQuantity.setText(String.valueOf(quantity));
		
		checkoutButton.setVisible(true);
		
		purchase1 = new AutoPartsPurchase(userId, 9, quantity, total);
		checkout();
		
    }

    @FXML
    void buyEngineOil(ActionEvent event) {
    	
    	int quantity = 0;
		try {
			quantity = Integer.parseInt(qtyField1.getText());
		} catch (Exception e) {
			showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
			return;
		}
		
		if (quantity <= 0) {
			showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
			return;
		}
		
		
		if (!purchaseDAO.checkQuantity(2, quantity)) {
            showAlert("Error", "Not enough quantity available", Alert.AlertType.ERROR);
            return ;
		}
		
		
		
		double total = quantity * 10;
		engineOilCost.setText("$" + total);
		engineOilQuantity.setText(String.valueOf(quantity));
		
		checkoutButton.setVisible(true);
		
		purchase2 = new AutoPartsPurchase(userId, 2, quantity, total);
		checkout();
		
    	
    }

    @FXML
    void buyFuelPump(ActionEvent event)
    {

		int quantity = 0;
		try {
			quantity = Integer.parseInt(qtyField3.getText());
		} catch (Exception e) {
			showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
			return;
		}
		
		if (quantity <= 0) {
			showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
			return;
		}
		
		if (!purchaseDAO.checkQuantity(8, quantity)) {
			showAlert("Error", "Not enough quantity available", Alert.AlertType.ERROR);
			return;
		}
		
		double total = quantity * 30;
		fuelpumpCost.setText("$" + total);
		fuelpumpQuantity.setText(String.valueOf(quantity));
		
		checkoutButton.setVisible(true);
		
		purchase3 = new AutoPartsPurchase(userId, 8, quantity, total);
		checkout();
		
    }

    @FXML
    void buyHeadLights(ActionEvent event)
    {
    	
    	        int quantity = 0;
    	        try {
					quantity = Integer.parseInt(qtyField2.getText());
				} catch (Exception e) {
					showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
					return;
				}
    	        
    	        
				if (quantity <= 0) {
					showAlert("Error", "Please enter a valid quantity", Alert.AlertType.ERROR);
					return;
				}
				
				
				if (!purchaseDAO.checkQuantity(6, quantity)) {
					showAlert("Error", "Not enough quantity available", Alert.AlertType.ERROR);
					return;
				}

				double total = quantity * 15;
				headlightsCost.setText("$" + total);
				headligthsQuantity.setText(String.valueOf(quantity));
					
				checkoutButton.setVisible(true);
				
				purchase4 = new AutoPartsPurchase(userId, 6, quantity, total);
				checkout();

	}
    	
    
    
    
    public void checkout()
    {
    	if(!(headligthsQuantity.getText().isEmpty()))
    	{
    		headlightsLabel.setVisible(true);
    		headlightsCost.setVisible(true);
    		headligthsQuantity.setVisible(true);
    		
    		
    		
    	}
    	
		if (!(fuelpumpQuantity.getText().isEmpty())) {
			fuelpumpLabel.setVisible(true);
			fuelpumpCost.setVisible(true);
			fuelpumpQuantity.setVisible(true);
		}
	
	
		if (!(engineOilQuantity.getText().isEmpty())) {
			engineOilLabel.setVisible(true);
			engineOilCost.setVisible(true);
			engineOilQuantity.setVisible(true);
		}
	    
		if (!(clutchplateQuantity.getText().isEmpty())) {
			clutchplateLabel.setVisible(true);
			clutchplateCost.setVisible(true);
			clutchplateQuantity.setVisible(true);
		}
		

    	
    	checkoutButton.setVisible(false);
    	clearCartButton.setVisible(true);
		double total = 0;
		if (clutchplateCost.isVisible()) {
			total += Double.parseDouble(clutchplateCost.getText().substring(1));
		}
		if (engineOilCost.isVisible()) {
			total += Double.parseDouble(engineOilCost.getText().substring(1));
		}
		if (fuelpumpCost.isVisible()) {
			total += Double.parseDouble(fuelpumpCost.getText().substring(1));
		}
		if (headlightsCost.isVisible()) {
			total += Double.parseDouble(headlightsCost.getText().substring(1));
		}
		
		totalCost.setText("$" + total);
		carpartInvoice.setVisible(true);
	
    }
    
    public void clearCart()
    {
    	headlightsLabel.setVisible(false);
    	headlightsCost.setVisible(false);
    	headlightsCost.setText("");
    	headligthsQuantity.setVisible(false);
    	headligthsQuantity.setText("");
    	
    	fuelpumpLabel.setVisible(false);
    	fuelpumpCost.setVisible(false);
    	fuelpumpCost.setText("");
    	fuelpumpQuantity.setVisible(false);
    	fuelpumpQuantity.setText("");
    	
    	engineOilLabel.setVisible(false);
    	engineOilCost.setVisible(false);
    	engineOilCost.setText("");
    	engineOilQuantity.setVisible(false);
    	engineOilQuantity.setText("");
    	
    	clutchplateLabel.setVisible(false);
    	clutchplateCost.setVisible(false);
    	clutchplateCost.setText("");
    	clutchplateQuantity.setVisible(false);
    	clutchplateQuantity.setText("");
    	
    	checkoutButton.setVisible(false);
    	totalCost.setText("");
    	carpartInvoice.setVisible(false);
    	clearCartButton.setVisible(false);
    }
    
    public void handlePayNow()
	{
		paymentAnchor.setVisible(true);
		carpartInvoice.setVisible(false);
		clearCartButton.setVisible(false);
		totalLabel.setText(totalCost.getText());
	}
    
    
    public void handlePayment() throws SQLException
    {
//		if (choosePaymentMethod.getSelectedToggle() == null) {
//			showAlert("Error", "Please choose a payment method", Alert.AlertType.ERROR);
//			return;
//		}

		 PaymentContext paymentContext = new PaymentContext();
		 PaymentProcessor paymentProcessor = null;

		    if (creditCardToggel.isSelected())
		    {
		        paymentProcessor = new CreditCardPayment(
		            nameField.getText(),
		            cardNumber.getText(),
		            expiryDate.getValue(),
		            cvv.getText()
		        );
		    }
		    else if (paypalToggel.isSelected())
		    {
		        paymentProcessor = new PayPalPayment(
		            emailField.getText(), 
		            passwordField.getText()    
		        );
		    }

		    paymentContext.setPaymentProcessor(paymentProcessor);

		    if (paymentProcessor != null) {
		        double total = Double.parseDouble(totalCost.getText().substring(1)); // Extract total cost
		        paymentContext.executePayment(total);
		        insertPurchase();
		        // Insert payment into database
		        Payment payment = new Payment(userId, total, paymentProcessor.getPaymentMethod());
		        PaymentDAO.insertPayment(payment);
		    } else {
		        showAlert("Error", "Invalid payment method.", Alert.AlertType.ERROR);
		    }
	
    }
    
	public void insertPurchase()
	{
		AutoPartPurchaseDAO purchaseDAO = new AutoPartPurchaseDAO();
		
		
		if (purchase1.getQuantity() > 0) {
			purchaseDAO.insertAutoPartPurchase(purchase1);
		}
		if (purchase2.getQuantity() > 0) {
			purchaseDAO.insertAutoPartPurchase(purchase2);
		}
		if (purchase3.getQuantity() > 0) {
			purchaseDAO.insertAutoPartPurchase(purchase3);
		}
		if (purchase4.getQuantity() > 0) {
			purchaseDAO.insertAutoPartPurchase(purchase4);
		}
	}
	
	public void creditCardOn()
	{
		creditCardTab.setVisible(true);
		payPalTab.setVisible(false);
		creditCardToggel.setSelected(true);
		paypalToggel.setSelected(false);
	}
	
	public void payPalOn() {
		creditCardTab.setVisible(false);
		payPalTab.setVisible(true);
		creditCardToggel.setSelected(false);
		paypalToggel.setSelected(true);
	}
    
	
	public void handleBack(ActionEvent event) throws IOException {
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

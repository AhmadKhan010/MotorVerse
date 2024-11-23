package services;

import java.time.LocalDate;

import javafx.scene.control.Alert;

public class CreditCardPayment implements PaymentProcessor {
    private String name;
    private String cardNumber;
    private LocalDate expiryDate;
    private String cvv;

    public CreditCardPayment(String name, String cardNumber, LocalDate expiryDate, String cvv) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean validate() {
        if (name == null || name.isEmpty() || cardNumber == null || cardNumber.isEmpty() 
            || expiryDate == null || cvv == null || cvv.isEmpty()) {
            System.out.println("Credit Card validation failed: Missing or invalid fields.");
            return false;
        }
        if (expiryDate.isBefore(LocalDate.now())) {
            System.out.println("Credit Card validation failed: Card has expired.");
            return false;
        }
        return true;
    }
    
    @Override
    public void processPayment(double amount) {
    		showAlert("Payment Processed", "Processing credit card payment of $" + amount + " for card number: " + cardNumber, Alert.AlertType.INFORMATION);
            showAlert("Success", "Payment processed successfully", Alert.AlertType.INFORMATION);
    }
    
    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }

	 private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	
}

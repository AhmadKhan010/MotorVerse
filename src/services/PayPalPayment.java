package services;

import java.util.regex.Pattern;

import javafx.scene.control.Alert;

public class PayPalPayment implements PaymentProcessor {
    private String email;
    private String password;

    public PayPalPayment(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean validate() {
        // Check if email is valid
        if (email == null || email.isEmpty() || !isValidEmail(email)) {
            System.out.println("PayPal validation failed: Invalid email format.");
            return false;
        }

        // Check if password is valid
        if (password == null || password.length() < 8) {
            System.out.println("PayPal validation failed: Password must be at least 8 characters long.");
            return false;
        }

        return true;
    }

    @Override
    public void processPayment(double amount) {
    	showAlert("Payment Processed", "Processing PayPal payment of $" + amount + " for email: " + email, Alert.AlertType.INFORMATION);
        showAlert("Success", "Payment processed successfully", Alert.AlertType.INFORMATION);
    }

    @Override
    public String getPaymentMethod() {
        return "PayPal";
    }
    
    
    // Helper method to validate email format using regex
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(emailRegex, email);
    }
    

	 private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
    
    
}

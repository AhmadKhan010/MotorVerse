
package services;

import javafx.scene.control.Alert;

public class PaymentContext {
    private PaymentProcessor paymentProcessor;

    public void setPaymentProcessor(PaymentProcessor paymentProcessor)
    {
        this.paymentProcessor = paymentProcessor;
    }

    public boolean executePayment(double amount) {
        if (paymentProcessor == null) {
            showAlert("Payment Error", "No payment processor set. Please select a payment method.", Alert.AlertType.ERROR);
            return false;
        }

        if (paymentProcessor.validate()) {
            paymentProcessor.processPayment(amount);
            return true;
        } else {
            showAlert("Payment Error", "Payment validation failed. Please check your payment details.", Alert.AlertType.ERROR);
            return false;
        }
    }
    

	 private void showAlert(String title, String message, Alert.AlertType type) {
	        Alert alert = new Alert(type);
	        alert.setTitle(title);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
}


package services;


public class PaymentContext {
    private PaymentProcessor paymentProcessor;

    public void setPaymentProcessor(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void executePayment(double amount) {
        if (paymentProcessor == null) {
            System.out.println("No payment method selected.");
            return;
        }

        if (paymentProcessor.validate()) {
            paymentProcessor.processPayment(amount);
        } else {
            System.out.println("Payment validation failed. Please try again.");
        }
    }
}

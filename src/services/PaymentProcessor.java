package services;

public interface PaymentProcessor {
    boolean validate();
    void processPayment(double amount);
    String getPaymentMethod();
}

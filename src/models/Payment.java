package models;

public class Payment {

	private int userId;
	private double amount;
	private String paymentMethod;
	
	public Payment(int userId, double amount, String paymentMethod) {
		this.userId = userId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
	}
	
	// Getters and Setters
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	
	
	
}

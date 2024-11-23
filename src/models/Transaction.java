package models;

public class Transaction {
    private int transactionId;
    private String user;
    private String vehicle;
    private String date;
    private double amount;

    public Transaction(int transactionId, String user, String vehicle, String date, double amount) {
        this.transactionId = transactionId;
        this.user = user;
        this.vehicle = vehicle;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getUser() {
        return user;
    }

    public String getVehicle() {
        return vehicle;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }
}

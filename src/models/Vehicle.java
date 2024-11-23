package models;

public class Vehicle {
    private int id;
    private String make;
    private String model;
    private int year;
    private double price;
    private double rentalPrice;
    private double averageRating;

    public Vehicle(String make, String model, int year, double price, double rentalPrice, double averageRating) {
        
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.rentalPrice = rentalPrice;
        this.averageRating = averageRating;
    }

    // Getters and Setters for all fields

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }

    public double getAverageRating() {
        return averageRating;
    }

    // toString method to display information if needed

    @Override
    public String toString() {
        return make + " " + model + " (" + year + ") - Price: $" + price;
    }
}

package models;

public class VehicleListingDTO {
    private int vehicleId;
    private String make;
    private String model;
    private int year;
    private double price;
    private String description; // From Listings table
    private String sellerName;  // From Users table
    private String listingType;
    private String imagePath;
    private double averageRating;

    // Constructor
    public VehicleListingDTO(int vehicleId, String make, String model, int year, double price, String description, String sellerName) {
        this.vehicleId = vehicleId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.description = description;
        this.sellerName = sellerName;
    }
    
    // Getters
    public int getVehicleId() { return vehicleId; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getSellerName() { return sellerName; }
    public String getListingType() { return listingType; }
    public void setListingType(String listingType) { this.listingType = listingType; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }
    public void setMake(String make) { this.make = make; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }
    public void setDescription(String description) { this.description = description; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    
}

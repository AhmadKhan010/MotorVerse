package models;

import java.math.BigDecimal;

public class Listing {
    private int listingId;
    private int sellerId;
    private int vehicleId;
    private BigDecimal price;
    private BigDecimal rentalPrice;
    private String description;
    private String listingType;
    private String imagePath;

    // Constructor
    public Listing(int listingId, int sellerId, int vehicleId, BigDecimal price, BigDecimal rentalPrice, String description, String listingType) {
        this.listingId = listingId;
        this.sellerId = sellerId;
        this.vehicleId = vehicleId;
        this.price = price;
        this.rentalPrice = rentalPrice;
        this.description = description;
        this.listingType = listingType;
    }
    
    public Listing( int sellerId, int vehicleId, BigDecimal price, BigDecimal rentalPrice, String description, String listingType,String imagePath) {
       
        this.sellerId = sellerId;
        this.vehicleId = vehicleId;
        this.price = price;
        this.rentalPrice = rentalPrice;
        this.description = description;
        this.listingType = listingType;
        this.imagePath = imagePath;
    }
    

    // Getters and Setters
    public int getListingId() {
        return listingId;
    }

    public void setListingId(int listingId) {
        this.listingId = listingId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getRentalPrice() {
        return rentalPrice;
    }

    public void setRentalPrice(BigDecimal rentalPrice) {
        this.rentalPrice = rentalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getListingType() {
        return listingType;
    }

    public void setListingType(String listingType) {
        this.listingType = listingType;
    }
    
	public String getImagePath() {
		// TODO Auto-generated method stub
		return imagePath;
	}
	
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}

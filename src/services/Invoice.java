package services;

import models.VehicleListingDTO;

public class Invoice {
	
	private double rentalPrice;
	private double price;
	private int days;
	private double premium;
	private double total;
	private String insuranceType;
	private VehicleListingDTO vehicle;
	
	public Invoice(double rentalPrice,double price, int days, String insuranceType, VehicleListingDTO vehicle) {
		this.rentalPrice = rentalPrice;
		this.price = price;
		this.days = days;
		this.insuranceType = insuranceType;
		this.vehicle = vehicle;
	}
}

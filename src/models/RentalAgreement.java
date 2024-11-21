package models;

public class RentalAgreement {
    private int rentalId;
    private int renterId;
    private String renterName;
    private int vehicleId;
    private String rentalPeriod;
    private double rentalCost;
    private String insuranceType;
    private double premium;
    private int sellerId;

    public RentalAgreement( int renterId, int vehicleId, String rentalPeriod, double rentalCost, String insuranceType, double premium, int sellerId) {
        
        this.renterId = renterId;
       // this.renterName = renterName;
        this.vehicleId = vehicleId;
        this.rentalPeriod = rentalPeriod;
        this.rentalCost = rentalCost;
        this.insuranceType = insuranceType;
        this.premium = premium;
        this.sellerId = sellerId;
    }

    public RentalAgreement(int rentalId,int renterId, String renterName, int vehicleId, String rentalPeriod, double rentalCost) {
		// TODO Auto-generated constructor stub
    			this.rentalId = rentalId;
    	        this.renterId = renterId;
    	        this.renterName = renterName;
    	        this.vehicleId = vehicleId;
    	        this.rentalPeriod = rentalPeriod;
    	        this.rentalCost = rentalCost;
	}
    
    public RentalAgreement(int rentalId,String renterName, int vehicleId, String rentalPeriod, double rentalCost) 
    {
		// TODO Auto-generated constructor stub
		this.rentalId = rentalId;
		this.renterName = renterName;
		this.vehicleId = vehicleId;
		this.rentalPeriod = rentalPeriod;
		this.rentalCost = rentalCost;
    	
    }
    
    
    
    

	// Getters and Setters
    public int getRentalId() {
        return rentalId;
    }

    public void setRentalId(int rentalId) {
        this.rentalId = rentalId;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(String rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public double getRentalCost() {
        return rentalCost;
    }

    public void setRentalCost(double rentalCost) {
        this.rentalCost = rentalCost;
    }

	public String getInsuranceType() {
		// TODO Auto-generated method stub
		return insuranceType;
	}
	
	public double getPremium() {
		// TODO Auto-generated method stub
		return premium;
	}
	
	public int getSellerId() {
		// TODO Auto-generated method stub
		return sellerId;
	}
	
	public void setInsuranceType(String insuranceType) {
		// TODO Auto-generated method stub
		this.insuranceType = insuranceType;
	}
	
	public void setPremium(double premium) {
		// TODO Auto-generated method stub
		this.premium = premium;
	}
	
	public void setSellerId(int sellerId) {
		// TODO Auto-generated method stub
		this.sellerId = sellerId;
	}
}

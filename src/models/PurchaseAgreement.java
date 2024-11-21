package models;

public class PurchaseAgreement {
    private int purchaseId;
    private int buyerId;
    private String buyerName;
    private int vehicleId;
    private String purchaseDate;
    private double purchasePrice;
    private String insuranceType;
    private double premium;
    private int sellerId;

    public PurchaseAgreement(int purchaseId, int buyerId, String buyerName, int vehicleId, String purchaseDate, double purchasePrice) {
        this.purchaseId = purchaseId;
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.vehicleId = vehicleId;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
    }
    
	public PurchaseAgreement(int buyerId, String buyerName, int vehicleId, String purchaseDate, double purchasePrice, String insuranceType, double premium,int sellerId) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.vehicleId = vehicleId;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
        this.insuranceType = insuranceType;
        this.premium = premium;
        this.sellerId = sellerId;
	}	
	
	public PurchaseAgreement(int buyerId, String buyerName, int vehicleId, String purchaseDate, double purchasePrice) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.vehicleId = vehicleId;
        this.purchaseDate = purchaseDate;
        this.purchasePrice = purchasePrice;
	}
    
    

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }
    
	public String getInsuranceType() {
		return insuranceType;
	}
	
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	
	public double getPremium() {
		return premium;
	}
	
	public void setPremium(double premium) {
		this.premium = premium;
	}
	
	public int getSellerId() {
		return sellerId;
	}
	
	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}
	
	
}

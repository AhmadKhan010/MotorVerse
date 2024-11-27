package models;

//import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutoPartsPurchase {

    private int purchaseId;
    private int userId;
    private int partId;
    private int quantity;
    private double totalPrice;
    private LocalDateTime purchaseDate;
    private String partName;

    // Constructors
    public AutoPartsPurchase() {
    	userId = 0;
    	quantity = 0;
    }
          
    public AutoPartsPurchase(int userId, int partId, int quantity, double totalPrice) {
        this.userId = userId;
        this.partId = partId;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
	public AutoPartsPurchase( int userId, int partId,String partName, int quantity, double totalPrice,
			LocalDateTime purchaseDate) {
		
		this.userId = userId;
		this.partId = partId;
		this.partName = partName;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.purchaseDate = purchaseDate;
	}

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
    
    public String getPartName() {
    	        return partName;
    }

    @Override
    public String toString() {
        return "AutoPartPurchase{" +
                "purchaseId=" + purchaseId +
                ", userId=" + userId +
                ", partId=" + partId +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                ", purchaseDate=" + purchaseDate +
                '}';
    }
}

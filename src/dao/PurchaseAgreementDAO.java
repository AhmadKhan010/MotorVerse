package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import models.PurchaseAgreement;
import utils.DatabaseConnection;

public class PurchaseAgreementDAO {

	PurchaseAgreement purchaseAgreement;
	
	public PurchaseAgreementDAO(PurchaseAgreement purchaseAgreement) {
		this.purchaseAgreement = purchaseAgreement;
	}
	
	private Map<String,Integer> insruanceId = Map.of(
			"Collision", 1
			,"PIP", 2
			,"Engine Protection", 3
			,"None", 4
			);
	
	public void insertPurchaseAgreement() 
	{
		String query = "INSERT INTO purchaseagreements(buyer_id,vehicle_id,purchase_date,purchase_price,insurance_id,total_premium,seller_id) VALUES(?,?,?,?,?,?,?)";
		try {Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, purchaseAgreement.getBuyerId());
			stmt.setInt(2, purchaseAgreement.getVehicleId());
			// Give current date
			stmt.setDate(3,	java.sql.Date.valueOf(java.time.LocalDate.now()) );
			//stmt.setDouble(3, purchaseAgreement.getPurchasePrice());
			stmt.setDouble(4, purchaseAgreement.getPurchasePrice());
			
				stmt.setInt(5, insruanceId.get(purchaseAgreement.getInsuranceType()));
				stmt.setDouble(6, purchaseAgreement.getPremium());
			
			stmt.setInt(7, purchaseAgreement.getSellerId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.PurchaseAgreement;
import models.RentalAgreement;
import utils.DatabaseConnection;

public class PurchaseAgreementDAO {

	PurchaseAgreement purchaseAgreement;
	
	private Map<Integer, String> insuranceType = Map.of(1, "Collision", 2, "PIP", 3, "Engine Protection", 4,
			"None");
	
	
	public PurchaseAgreementDAO(PurchaseAgreement purchaseAgreement) {
		this.purchaseAgreement = purchaseAgreement;
	}
	
	public PurchaseAgreementDAO() {
		// TODO Auto-generated constructor stub
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
	
	public List<PurchaseAgreement> getPurchaseAgreements(int buyerId) {
		// TODO Auto-generated method stub

		String query = "SELECT * FROM purchaseagreements WHERE buyer_id = ? ";
		try {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, buyerId);
			ResultSet rs = stmt.executeQuery();
			List<PurchaseAgreement> purchaseAgreements = new ArrayList<PurchaseAgreement>();
			while (rs.next()) {
				//Using this constructor: public PurchaseAgreement(int buyerId, String buyerName, int vehicleId, String purchaseDate, double purchasePrice, String insuranceType, double premium,int sellerId) {
			   
				PurchaseAgreement purchaseAgreement = new PurchaseAgreement(rs.getInt("buyer_id")," ", rs.getInt("vehicle_id"), rs.getString("purchase_date"), rs.getDouble("purchase_price"), insuranceType.get(rs.getInt("insurance_id")), rs.getDouble("total_premium"), rs.getInt("seller_id"));
				purchaseAgreements.add(purchaseAgreement);
				
			}
			return purchaseAgreements;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
}

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.RentalAgreement;
import utils.DatabaseConnection;

public class RentalAgreementDAO {

	
	RentalAgreement  rentalAgreement;
	
	private Map<String,Integer> insuranceId = Map.of(
			"Collision", 1
			,"PIP", 2
			,"Engine Protection", 3
			,"None", 4
			);
			private Map<Integer, String> insuranceType = Map.of(1, "Collision", 2, "PIP", 3, "Engine Protection", 4,
					"None");
			
			
	
	
	public RentalAgreementDAO(RentalAgreement rentalAgreement) {
		this.rentalAgreement = rentalAgreement;
	}
	
	public RentalAgreementDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public void insertRentalAgreement() 
	{
		String query = "INSERT INTO rentalagreements(renter_id,vehicle_id,rental_period,rental_cost,insurance_id,total_premium,seller_id) VALUES(?,?,?,?,?,?,?)";
		try {Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, rentalAgreement.getRenterId());
			stmt.setInt(2, rentalAgreement.getVehicleId());
			stmt.setString(3, rentalAgreement.getRentalPeriod());
			stmt.setDouble(4, rentalAgreement.getRentalCost());
			
				stmt.setInt(5, insuranceId.get(rentalAgreement.getInsuranceType()));
				stmt.setDouble(6, rentalAgreement.getPremium());
			
			stmt.setInt(7, rentalAgreement.getSellerId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	// Return all rental agreements for a renter
	public List<RentalAgreement> getRentalAgreements(int renterId, String returnStatus) {
		
		String query;
		
		if (returnStatus.equals("All")) {
			query = "SELECT * FROM rentalagreements WHERE renter_id = ?";
		} else {
			query = "SELECT * FROM rentalagreements WHERE renter_id = ? AND return_status = ?";
		}
		
		
		try {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, renterId);
			if (!returnStatus.equals("All")) 
                stmt.setString(2, returnStatus);
			ResultSet rs = stmt.executeQuery();
			List<RentalAgreement> rentalAgreements = new ArrayList<RentalAgreement>();
			while (rs.next()) {
				RentalAgreement ra = new RentalAgreement(rs.getInt(1),rs.getInt(2), rs.getInt(3), rs.getString(4), rs.getDouble(5), insuranceType.get(rs.getInt(6)), rs.getDouble(7), rs.getInt(8));
				ra.setReturn_status(rs.getString(9));
				rentalAgreements.add(ra);
				
			}
			return rentalAgreements;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public void updateReturnStatus(int rentalId) {
		String query = "UPDATE rentalagreements SET return_status = 'Returned' WHERE rental_id = ?";
		try {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, rentalId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
}

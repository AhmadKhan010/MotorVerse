package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import models.RentalAgreement;
import utils.DatabaseConnection;

public class RentalAgreementDAO {

	
	RentalAgreement  rentalAgreement;
	
	private Map<String,Integer> insruanceId = Map.of(
			"Collision", 1
			,"PIP", 2
			,"Engine Protection", 3
			,"None", 4
			);
			
			
	
	
	public RentalAgreementDAO(RentalAgreement rentalAgreement) {
		this.rentalAgreement = rentalAgreement;
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
			
				stmt.setInt(5, insruanceId.get(rentalAgreement.getInsuranceType()));
				stmt.setDouble(6, rentalAgreement.getPremium());
			
			stmt.setInt(7, rentalAgreement.getSellerId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}

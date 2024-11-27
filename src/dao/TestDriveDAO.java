package dao;

import java.sql.Connection;
import java.sql.SQLException;

import models.TestDrive;
import utils.DatabaseConnection;

public class TestDriveDAO {

	
	TestDrive testDrive;
	
	public TestDriveDAO() {

	}
	
	public void insertTestDrive(TestDrive testDrive) {
		
		String query = "INSERT INTO testdrives (user_id, vehicle_id, schedule_date) VALUES (" + testDrive.getUserID() + ", " + testDrive.getVehicleID() + ", '" + testDrive.getDate() + "')";
    
		// Execute query here
		try {
			Connection conn = DatabaseConnection.getConnection();
			conn.createStatement().executeUpdate(query);
	
			// Execute query here
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	
	
}

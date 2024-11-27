package models;

public class TestDrive {

	private int userID;
	private int vehicleID;
	private String date;
	
	public TestDrive(int userID, int vehicleID, String date) {
		this.userID = userID;
		this.vehicleID = vehicleID;
		this.date = date;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public int getVehicleID() {
		return vehicleID;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public void setVehicleID(int vehicleID) {
		this.vehicleID = vehicleID;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	
	
}

package controllers;

import dao.TestDriveDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import models.TestDrive;
import utils.SessionManager;

public class TestDriveController {

    @FXML
    private Button bookButton;

    @FXML
    private DatePicker dateChooser;

    @FXML
    private BorderPane rootPane;
    @FXML Label carName;
    
    private int vehicleId;
    private String vehicleName;

    
	public void initialize() {
		
		
	}
    
    @FXML
    void handleBack(ActionEvent event) {
    	//Close the rootPane
    	rootPane.setVisible(false);
    }

    @FXML
    void handleBookButton(ActionEvent event) {
    	int userId = SessionManager.getInstance().getCurrentUser().getUserId();
    	TestDrive testDrive = new TestDrive(userId, vehicleId, dateChooser.getValue().toString());
    	TestDriveDAO testDriveDAO = new TestDriveDAO();
    	testDriveDAO.insertTestDrive(testDrive);
    	
    }
    
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
    
	public void setVehicleName(String vehicleName) {
		this.carName.setText(vehicleName);
		this.vehicleName = vehicleName;
	}
    

}

package controllers;

import java.io.IOException;

import dao.CustomerSupportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.CustomerSupport;
import utils.SessionManager;

public class CustomerSupportController {

	
	
	
    @FXML
    private TextField descriptionField;

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField subjectField;

    @FXML
    private Button submitButton;

    @FXML
    private AnchorPane myTicketsPane, issueTicketPane;
    
    @FXML
    private TableColumn<CustomerSupport, String> descriptionColumn;
    @FXML
    private TableColumn<CustomerSupport, String> statusColumn;
    @FXML
    private TableColumn<CustomerSupport, String> ticketIdColumn;
    
    @FXML
    private TableView<CustomerSupport> pendingTickets;
    
    @FXML
    private TableView<CustomerSupport> closedTickets;
    
    
	public void initialize() {
		issueTicketPane.setVisible(true);
		myTicketsPane.setVisible(false);
		
		descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
        
		
		CustomerSupportDAO customerSupportDAO = new CustomerSupportDAO();
		
		 int currentUserId = SessionManager.getInstance().getCurrentUser().getUserId();
	        ObservableList<CustomerSupport> tickets = FXCollections.observableArrayList(
	            customerSupportDAO.getPendingTicketsForUser(currentUserId)
	        );
	        pendingTickets.setItems(tickets);
	     
			ObservableList<CustomerSupport> closedTicketsList = FXCollections
					.observableArrayList(customerSupportDAO.getClosedTicketsForUser(currentUserId));
			closedTickets.setItems(closedTicketsList);
			
	        
	}
    
    
    
    public void handleBack(ActionEvent event)  {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
		Parent root = null;
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Stage stage = (Stage) rootPane.getScene().getWindow();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("User Dashboard - ");
		stage.show();
    }

    @FXML
    void handleSubmit(ActionEvent event) {
    	String subject = subjectField.getText();
    	String description = descriptionField.getText();
    	
    	CustomerSupport customerSupport = new CustomerSupport(SessionManager.getInstance().getCurrentUser().getUserId(), description, subject);
    	CustomerSupportDAO customerSupportDAO = new CustomerSupportDAO();
    	customerSupportDAO.insertCustomerSupport(customerSupport);
    	
    	showAlert("Success", "Your Issue has been noted. Ticket submitted successfully", Alert.AlertType.INFORMATION);
    }

    @FXML
    void issueTicketOn(ActionEvent event) {
    	issueTicketPane.setVisible(true);
    	myTicketsPane.setVisible(false);
    }

    @FXML
    void myTicketOn(ActionEvent event) {
    	 int currentUserId = SessionManager.getInstance().getCurrentUser().getUserId();
	        CustomerSupportDAO customerSupportDAO = new CustomerSupportDAO();
			ObservableList<CustomerSupport> tickets = FXCollections.observableArrayList(
	            customerSupportDAO .getPendingTicketsForUser(currentUserId)
	        );
			
	    pendingTickets.setItems(tickets);
		issueTicketPane.setVisible(false);
		myTicketsPane.setVisible(true);
    }
    
    public void handleBack()
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserDashboard.fxml"));
    	        Parent root = null;
    	                        try {
									root = loader.load();
									Stage stage = (Stage) rootPane.getScene().getWindow();
									Scene scene = new Scene(root);
									scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
									stage.setScene(scene);
									stage.setTitle("User Dashboard - ");
									stage.show();
									
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
    	                        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    
}

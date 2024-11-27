package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.CustomerSupport;
import utils.DatabaseConnection;

public class CustomerSupportDAO {

	
	
	CustomerSupport customerSupport;
	
	public CustomerSupportDAO() {

	}
	
	
	
	public void insertCustomerSupport(CustomerSupport customerSupport) 
	{
		// Insert the customer support into the database
		
		String query = "INSERT INTO CustomerSupportTickets (user_id, issue_description,status) VALUES (?, ?,?)";
		try {
			// Prepare the query
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, customerSupport.getUserId());
			preparedStatement.setString(2, customerSupport.getDescription());
			preparedStatement.setString(3, "Open");
			
			// Execute the query
			preparedStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();		
		
			}
		
	}
	
	 public List<CustomerSupport> getPendingTicketsForUser(int userId)
	 {
	        List<CustomerSupport> tickets = new ArrayList<>();
	        String query = "SELECT support_id,user_id, issue_description, status FROM CustomerSupportTickets WHERE user_id = ? AND status = 'Open'";

	        try (Connection connection = DatabaseConnection.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	            preparedStatement.setInt(1, userId);
	            ResultSet resultSet = preparedStatement.executeQuery();

	            while (resultSet.next()) {
	                CustomerSupport ticket = new CustomerSupport(
	                		resultSet.getInt("support_id"),
	                    resultSet.getInt("user_id"),
	                    resultSet.getString("issue_description"),
	                    resultSet.getString("status")
	                );
	                tickets.add(ticket);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return tickets;
	    }
	
		public List<CustomerSupport> getClosedTicketsForUser(int userId) {
			List<CustomerSupport> tickets = new ArrayList<>();
			String query = "SELECT support_id,user_id, issue_description, status FROM CustomerSupportTickets WHERE user_id = ? AND status = 'Closed'";

			try (Connection connection = DatabaseConnection.getConnection();
					PreparedStatement preparedStatement = connection.prepareStatement(query)) {
				preparedStatement.setInt(1, userId);
				ResultSet resultSet = preparedStatement.executeQuery();

				while (resultSet.next()) {
					CustomerSupport ticket = new CustomerSupport(resultSet.getInt("support_id"),resultSet.getInt("user_id"),
							resultSet.getString("issue_description"), resultSet.getString("status"));
					tickets.add(ticket);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return tickets;
		}
	
	

	
	
}

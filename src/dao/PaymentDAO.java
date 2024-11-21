package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import models.Payment;
import utils.DatabaseConnection;

public class PaymentDAO {

	Payment payment;
	
	public PaymentDAO(Payment payment) {
		this.payment = payment;
	}
	
	public static void insertPayment(Payment payment) throws SQLException {
		String query = "INSERT INTO Payments(user_id,amount,payment_method) VALUES(?,?,?)";
		// Execute the query
		
		Connection conn = DatabaseConnection.getConnection();
		PreparedStatement ps = conn.prepareStatement(query);
		ps.setInt(1, payment.getUserId());
		ps.setDouble(2, payment.getAmount());
		ps.setString(3, payment.getPaymentMethod());
		//ps.setDate(4, java.sql.Date.valueOf(java.time.LocalDate.now()));
		ps.executeUpdate();
		ps.close();
		conn.close();
			
	}
	
	
}

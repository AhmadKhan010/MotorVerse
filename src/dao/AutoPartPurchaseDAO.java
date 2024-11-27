package dao;

import models.AutoPartsPurchase;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AutoPartPurchaseDAO {

    public boolean insertAutoPartPurchase(AutoPartsPurchase purchase) {
        String query = "INSERT INTO AutoPartPurchases (user_id, part_id, quantity, total_price) VALUES (?, ?, ?, ?)";
      //Deduct the quantity from the AutoParts table
        String updateQuery = "UPDATE AutoParts SET quantity = quantity - ? WHERE part_id = ?";
		
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set query parameters
            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getPartId());
            stmt.setInt(3, purchase.getQuantity());
            stmt.setDouble(4, purchase.getTotalPrice());
            
            // Execute the query
            int rowsInserted = stmt.executeUpdate();
            
            // Update the AutoParts table
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.setInt(1, purchase.getQuantity());
            updateStmt.setInt(2, purchase.getPartId());
            updateStmt.executeUpdate();
            
            
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<AutoPartsPurchase> getAutoPartsPurchases(int userId) {
        List<AutoPartsPurchase> purchases = new ArrayList<>();
        String query = """
            SELECT 
                ap.part_id, ap.quantity, ap.total_price,ap.purchase_date, p.name AS part_name 
            FROM AutoPartPurchases ap
            JOIN AutoParts p ON ap.part_id = p.part_id
            WHERE ap.user_id = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                AutoPartsPurchase purchase = new AutoPartsPurchase(
                    userId,
                    rs.getInt("part_id"),
                    rs.getString("part_name"),
                    rs.getInt("quantity"),
                    rs.getDouble("total_price"),
                    rs.getTimestamp("purchase_date").toLocalDateTime()
                );
                purchases.add(purchase);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }
    
	public boolean checkQuantity(int partId, int quantity) {
        String query = "SELECT quantity FROM AutoParts WHERE part_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, partId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantity") >= quantity;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
        }
    
    
    
    
}

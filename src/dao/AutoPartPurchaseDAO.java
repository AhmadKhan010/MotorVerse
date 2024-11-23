package dao;

import models.AutoPartsPurchase;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AutoPartPurchaseDAO {

    public boolean insertAutoPartPurchase(AutoPartsPurchase purchase) {
        String query = "INSERT INTO AutoPartPurchases (user_id, part_id, quantity, total_price) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set query parameters
            stmt.setInt(1, purchase.getUserId());
            stmt.setInt(2, purchase.getPartId());
            stmt.setInt(3, purchase.getQuantity());
            stmt.setDouble(4, purchase.getTotalPrice());
            
            // Execute the query
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

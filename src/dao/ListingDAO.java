package dao;

import models.Listing;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListingDAO {

    /**
     * Get all listings from the database.
     */
    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<>();
        String query = "SELECT listing_id, seller_id, vehicle_id, price, rental_price, description, listing_type FROM Listings";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                listings.add(new Listing(
                    rs.getInt("listing_id"),
                    rs.getInt("seller_id"),
                    rs.getInt("vehicle_id"),
                    rs.getBigDecimal("price"),
                    rs.getBigDecimal("rental_price"),
                    rs.getString("description"),
                    rs.getString("listing_type")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    /**
     * Search listings by listing type.
     */
    public List<Listing> searchListings(String listingType) {
        List<Listing> listings = new ArrayList<>();
        String query = "SELECT listing_id, seller_id, vehicle_id, price, rental_price, description, listing_type FROM Listings WHERE listing_type = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, listingType);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    listings.add(new Listing(
                        rs.getInt("listing_id"),
                        rs.getInt("seller_id"),
                        rs.getInt("vehicle_id"),
                        rs.getBigDecimal("price"),
                        rs.getBigDecimal("rental_price"),
                        rs.getString("description"),
                        rs.getString("listing_type")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listings;
    }

    /**
     * Delete a listing by ID.
     */
    public boolean deleteListing(int listingId) {
        String query = "DELETE FROM Listings WHERE listing_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, listingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void insertListing(Listing listing)
    {
		String query = """
				    INSERT INTO Listings (seller_id, vehicle_id, price, rental_price, description, listing_type, image_path)
				    VALUES (?, ?, ?, ?, ?, ?,?)
				""";

		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(query)) {

			stmt.setInt(1, listing.getSellerId());
			stmt.setInt(2, listing.getVehicleId());
			stmt.setBigDecimal(3, listing.getPrice());
			stmt.setBigDecimal(4, listing.getRentalPrice());
			stmt.setString(5, listing.getDescription());
			stmt.setString(6, listing.getListingType());
			stmt.setString(7, listing.getImagePath());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
    
}

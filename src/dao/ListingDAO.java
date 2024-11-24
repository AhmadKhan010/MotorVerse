package dao;

import models.Listing;
import models.VehicleListingDTO;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

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
    public List<VehicleListingDTO> getListingsForCurrentUser(int userId) {
        List<VehicleListingDTO> vehicleListings = new ArrayList<>();
        String query = """
            SELECT 
                v.vehicle_id, v.make, v.model, v.year, l.price, v.image_path, 
                l.description, l.listing_type, 
                u.name AS seller_name
            FROM Listings l
            JOIN Vehicles v ON l.vehicle_id = v.vehicle_id
            JOIN Users u ON l.seller_id = u.user_id
            WHERE l.seller_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VehicleListingDTO dto = new VehicleListingDTO(
                    rs.getInt("vehicle_id"),
                    rs.getString("make"),
                    rs.getString("model"),
                    rs.getInt("year"),
                    rs.getDouble("price"),
                    rs.getString("description"),
                    rs.getString("seller_name")
                );
                dto.setListingType(rs.getString("listing_type"));
                dto.setImagePath(rs.getString("image_path"));
                vehicleListings.add(dto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicleListings;
    }

    public boolean updateVehicleListing(VehicleListingDTO dto) {
        String findVehicleQuery = """
            SELECT vehicle_id 
            FROM Vehicles 
            WHERE make = ? AND model = ? AND year = ? AND price = ? AND image_path = ?
        """;

        String insertVehicleQuery = """
            INSERT INTO Vehicles (make, model, year, price, image_path) 
            VALUES (?, ?, ?, ?, ?)
        """;
        
        String updateListingQuery;

        if(dto.getListingType().equals("Selling"))
         updateListingQuery = """
            UPDATE Listings 
            SET vehicle_id = ?,price = ?, description = ? 
            WHERE listing_id = ?
        """;
		else
			updateListingQuery = """
					    UPDATE Listings
					    SET vehicle_id = ?, rental_price = ?, description = ?, 
					    WHERE listing_id = ?
					""";

        String updateVehicleQuery = """
            UPDATE Vehicles 
            SET make = ?, model = ?, year = ?, price = ?, image_path = ?
            WHERE vehicle_id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            int vehicleId;
            try (PreparedStatement findStmt = conn.prepareStatement(findVehicleQuery)) {
                // Check if the updated vehicle already exists
                findStmt.setString(1, dto.getMake());
                findStmt.setString(2, dto.getModel());
                findStmt.setInt(3, dto.getYear());
                findStmt.setDouble(4, dto.getPrice());
                findStmt.setString(5, dto.getImagePath());

                ResultSet rs = findStmt.executeQuery();
                if (rs.next()) {
                    // Vehicle already exists, get the existing vehicle_id
                    vehicleId = rs.getInt("vehicle_id");
                } else {
                    // Vehicle does not exist, create a new vehicle
                    try (PreparedStatement insertStmt = conn.prepareStatement(insertVehicleQuery, Statement.RETURN_GENERATED_KEYS)) {
                        insertStmt.setString(1, dto.getMake());
                        insertStmt.setString(2, dto.getModel());
                        insertStmt.setInt(3, dto.getYear());
                        insertStmt.setDouble(4, dto.getPrice());
                        insertStmt.setString(5, dto.getImagePath());
                        insertStmt.executeUpdate();

                        // Retrieve the newly created vehicle_id
                        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            vehicleId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve vehicle_id for the new vehicle.");
                        }
                    }
                }
            }

            // Update the Listings table to reference the vehicle_id
            try (PreparedStatement listingStmt = conn.prepareStatement(updateListingQuery)) {
                listingStmt.setInt(1, vehicleId);
                listingStmt.setDouble(2, dto.getPrice());
                listingStmt.setString(3, dto.getDescription());
                listingStmt.setInt(4, dto.getVehicleId());
                listingStmt.executeUpdate();
            }

//            // Optionally update the original vehicle entry (if the DTO was meant to modify an existing vehicle)
//            try (PreparedStatement vehicleStmt = conn.prepareStatement(updateVehicleQuery)) {
//                vehicleStmt.setString(1, dto.getMake());
//                vehicleStmt.setString(2, dto.getModel());
//                vehicleStmt.setInt(3, dto.getYear());
//                vehicleStmt.setDouble(4, dto.getPrice());
//                vehicleStmt.setString(5, dto.getImagePath());
//                vehicleStmt.setInt(6, dto.getVehicleId());
//                vehicleStmt.executeUpdate();
//            }

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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

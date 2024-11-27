package dao;

import models.User;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

	
	
	User user;
	
	public UserDAO() {
		
	}
	
	public UserDAO(User user) {
		this.user = user;
	}
	
	
    // Insert user into the database
    public boolean insertUser(User user) {
        String sql = "INSERT INTO Users (name, email, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getPhoneNumber());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getRole());

            // Execute the query
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Return true if insertion was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET name = ?, email = ?, phone_number = ?, address = ?, password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhoneNumber());
            stmt.setString(4, user.getAddress());
            stmt.setString(5, user.getPassword());
            stmt.setInt(6, user.getUserId());

            // Execute the update query
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    // Check if email already exists in the database
	public boolean checkEmail(String email) {
		String sql = "SELECT * FROM Users WHERE email = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);
			return stmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// Check if name already exists in the database
	public boolean checkName(String name) {
		String sql = "SELECT * FROM Users WHERE name = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			return stmt.executeQuery().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//Return true if user with name and password exists in the database
	public boolean checkUser(String name,String password)
	{
		
		String sql = "SELECT * FROM Users WHERE name = ? AND password = ? ";
		try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
			// Set query parameters
            stmt.setString(1, name);
            stmt.setString(2, password);

            // Execute query
            ResultSet rs = stmt.executeQuery();

            // Check if user exists
            if (rs.next()) {
                int count = rs.getInt(1); // Get the first column from the result
                return count > 0; // If count > 0, user exists
            } else 
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
		
        }
	}
	
	//Return user object with provided name
	public User getUser(String name) {
		String sql = "SELECT * FROM Users WHERE name = ?";
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("name"), rs.getString("email"),
						rs.getString("password"), rs.getString("phone_number"), rs.getString("address"),
						rs.getString("role"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
    
}

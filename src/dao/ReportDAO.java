package dao;

import models.PurchaseAgreement;
import models.RentalAgreement;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    public List<PurchaseAgreement> getAllPurchaseAgreements() {
        List<PurchaseAgreement> list = new ArrayList<>();
        String query = """
            SELECT pa.purchase_id, u.name AS buyerName, pa.vehicle_id, pa.purchase_date, pa.purchase_price
            FROM PurchaseAgreements pa
            JOIN Users u ON pa.buyer_id = u.user_id;
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new PurchaseAgreement(
                        rs.getInt("purchase_id"),
                        rs.getString("buyerName"),
                        rs.getInt("vehicle_id"),
                        rs.getString("purchase_date"),
                        rs.getDouble("purchase_price")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<RentalAgreement> getAllRentalAgreements() {
        List<RentalAgreement> list = new ArrayList<>();
        String query = """
            SELECT ra.rental_id, u.name AS renterName, ra.vehicle_id, ra.rental_period, ra.rental_cost
            FROM RentalAgreements ra
            JOIN Users u ON ra.renter_id = u.user_id;
        """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new RentalAgreement(
                        rs.getInt("rental_id"),
                        rs.getString("renterName"),
                        rs.getInt("vehicle_id"),
                        rs.getString("rental_period"),
                        rs.getDouble("rental_cost")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}

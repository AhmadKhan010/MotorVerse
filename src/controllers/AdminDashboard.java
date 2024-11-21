package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import dao.AdminDAO;
import utils.DatabaseConnection;
import dao.ListingDAO;
import dao.ReportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Listing;
import models.Transaction;
import models.User;

public class AdminDashboard {
	
	

    @FXML
    private AnchorPane userManagementPane;
    

    @FXML
    private AnchorPane listingManagementPane;

    @FXML
    private AnchorPane transactionManagementPane;

    @FXML
    private AnchorPane reportsPane;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> colUserId;

    @FXML
    private TableColumn<User, String> colName;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colPhone;

    @FXML
    private TableColumn<User, String> colRole;

    @FXML
    private TableColumn<User, Integer> userIdColumn;

    @FXML
    private TableColumn<User, String> userNameColumn;

    @FXML
    private TableColumn<User, String> userEmailColumn;

    @FXML
    private TableColumn<User, String> userRoleColumn;

    @FXML
    private TableColumn<User, String> userStatusColumn;

    @FXML
    private TextField searchField;

    // Listing Management components
    @FXML
    private TableView<Listing> listingTable;

    @FXML
    private TableColumn<Listing, Integer> colListingId;

    @FXML
    private TableColumn<Listing, Integer> colSellerId;

    @FXML
    private TableColumn<Listing, Integer> colVehicleId;

    @FXML
    private TableColumn<Listing, String> colDescription;

    @FXML
    private TableColumn<Listing, String> colListingType;

    @FXML
    private TableColumn<Listing, Double> colPrice;

    @FXML
    private TableColumn<Listing, Double> colRentalPrice;

    @FXML
    private TextField listingSearchField;

    private AdminDAO adminDAO = new AdminDAO();
    private ListingDAO listingDAO = new ListingDAO();
    private ObservableList<User> userList = FXCollections.observableArrayList();
    private ObservableList<Listing> listingList = FXCollections.observableArrayList();
    
    // Transaction Management
    @FXML
    private ComboBox<String> transactionTypeComboBox;

    @FXML
    private TableView<Transaction> transactionTableView;

    @FXML
    private TableColumn<Transaction, Integer> transactionIdColumn;

    @FXML
    private TableColumn<Transaction, Integer> colTransactionListingId;

    @FXML
    private TableColumn<Transaction, Integer> colTransactionSellerId;

    @FXML
    private TableColumn<Transaction, Integer> colTransactionVehicleId;

    @FXML
    private TableColumn<Transaction, String> colTransactionType;

    @FXML
    private TableColumn<Transaction, Double> colTransactionPrice;

    @FXML
    private TableColumn<Transaction, Double> colTransactionRentalPrice;

    @FXML
    private TableColumn<Transaction, String> colTransactionDescription;
    
    @FXML
    private Button searchTransactionButton; // Added missing field

    @FXML
    private Button deleteTransactionButton; // Added missing field

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Transaction, String> userColumn;

    @FXML
    private TableColumn<Transaction, String> vehicleColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private ComboBox<String> reportTypeComboBox;

    @FXML
    private TableView<Object> reportTableView;

    private ReportDAO reportDAO = new ReportDAO();
    
    /**
     * Shows the User Management section.
     */
    @FXML
    public void showUserManagement() {
        hideAllPanes();
        userManagementPane.setVisible(true);
    }

    /**
     * Shows the Listing Management section.
     */
    @FXML
    public void showListingManagement() {
        hideAllPanes();
        listingManagementPane.setVisible(true);
    }

    /**
     * Shows the Transaction Management section.
     */
    @FXML
    public void showTransactionManagement() {
        hideAllPanes();
        transactionManagementPane.setVisible(true);
    }

    /**
     * Shows the Reports section.
     */
    @FXML
    public void showReports() {
        hideAllPanes();
        reportsPane.setVisible(true);
    }
    
    /**
     * Handles the logout functionality.
     */
    @FXML
    public void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/AdminLogin.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void initialize() {
        // Initialize User Management
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadUsers();

        // Initialize Listing Management
        colListingId.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        colSellerId.setCellValueFactory(new PropertyValueFactory<>("sellerId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colListingType.setCellValueFactory(new PropertyValueFactory<>("listingType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colRentalPrice.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));

        loadListings();
        

        // Load all listings on startup
        resetListingTable();
        
        // Initialize Transaction Management
        transactionTypeComboBox.getItems().addAll("Payments", "Rentals", "Purchases");
        transactionTypeComboBox.setOnAction(e -> loadTransactions());

        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicle"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        transactionTableView.setItems(transactionList);
        
        // Populate report types
        reportTypeComboBox.getItems().addAll("Purchase Agreements", "Rental Agreements");
        reportTypeComboBox.setValue("Purchase Agreements"); // Default selection
    }
    
    
    public void generateReport() {
        String selectedReportType = reportTypeComboBox.getValue();
        if ("Purchase Agreements".equals(selectedReportType)) {
            showPurchaseAgreements();
        } else if ("Rental Agreements".equals(selectedReportType)) {
            showRentalAgreements();
        }
    }

    private void showPurchaseAgreements() {
        ObservableList<Object> data = FXCollections.observableArrayList(reportDAO.getAllPurchaseAgreements());

        reportTableView.getColumns().clear();

        TableColumn<Object, Integer> purchaseIdCol = new TableColumn<>("Purchase ID");
        purchaseIdCol.setCellValueFactory(new PropertyValueFactory<>("purchaseId"));

        TableColumn<Object, String> buyerNameCol = new TableColumn<>("Buyer Name");
        buyerNameCol.setCellValueFactory(new PropertyValueFactory<>("buyerName"));

        TableColumn<Object, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));

        TableColumn<Object, String> purchaseDateCol = new TableColumn<>("Purchase Date");
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

        TableColumn<Object, Double> purchasePriceCol = new TableColumn<>("Purchase Price");
        purchasePriceCol.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));

        reportTableView.getColumns().addAll(purchaseIdCol, buyerNameCol, vehicleIdCol, purchaseDateCol, purchasePriceCol);

        reportTableView.setItems(data);
    }

    private void showRentalAgreements() {
        ObservableList<Object> data = FXCollections.observableArrayList(reportDAO.getAllRentalAgreements());

        reportTableView.getColumns().clear();

        TableColumn<Object, Integer> rentalIdCol = new TableColumn<>("Rental ID");
        rentalIdCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));

        TableColumn<Object, String> renterNameCol = new TableColumn<>("Renter Name");
        renterNameCol.setCellValueFactory(new PropertyValueFactory<>("renterName"));

        TableColumn<Object, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));

        TableColumn<Object, String> rentalPeriodCol = new TableColumn<>("Rental Period");
        rentalPeriodCol.setCellValueFactory(new PropertyValueFactory<>("rentalPeriod"));

        TableColumn<Object, Double> rentalCostCol = new TableColumn<>("Rental Cost");
        rentalCostCol.setCellValueFactory(new PropertyValueFactory<>("rentalCost"));

        reportTableView.getColumns().addAll(rentalIdCol, renterNameCol, vehicleIdCol, rentalPeriodCol, rentalCostCol);

        reportTableView.setItems(data);
    }

    
    // Transaction Management Functions
    @FXML
    public void loadTransactions() {
        String selectedType = transactionTypeComboBox.getValue();
        transactionList.clear();

        String query = "";
        if ("Purchases".equals(selectedType)) {
            query = """
                SELECT 
                    pa.purchase_id AS transaction_id,
                    u_buyer.name AS user,
                    CONCAT(v.make, ' ', v.model) AS vehicle,
                    pa.purchase_date AS date,
                    pa.purchase_price AS amount
                FROM 
                    PurchaseAgreements pa
                JOIN 
                    Users u_buyer ON pa.buyer_id = u_buyer.user_id
                JOIN 
                    Vehicles v ON pa.vehicle_id = v.vehicle_id;
            """;
        } else if ("Rentals".equals(selectedType)) {
            query = """
                SELECT 
                    ra.rental_id AS transaction_id,
                    u_renter.name AS user,
                    CONCAT(v.make, ' ', v.model) AS vehicle,
                    ra.rental_period AS date,
                    ra.rental_cost AS amount
                FROM 
                    RentalAgreements ra
                JOIN 
                    Users u_renter ON ra.renter_id = u_renter.user_id
                JOIN 
                    Vehicles v ON ra.vehicle_id = v.vehicle_id;
            """;
        } else if ("Payments".equals(selectedType)) {
            query = """
                SELECT 
                    p.payment_id AS transaction_id,
                    u.name AS user,
                    'N/A' AS vehicle,
                    p.payment_date AS date,
                    p.amount
                FROM 
                    Payments p
                JOIN 
                    Users u ON p.user_id = u.user_id;
            """;
        }

        if (!query.isEmpty()) {
            try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    transactionList.add(new Transaction(
                            rs.getInt("transaction_id"),
                            rs.getString("user"),
                            rs.getString("vehicle"),
                            rs.getString("date"),
                            rs.getDouble("amount")
                    ));
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to load transactions.", Alert.AlertType.ERROR);
            }
        }
    }


    
    
    @FXML
    public void deleteTransaction() {
        Transaction selectedTransaction = transactionTableView.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert("Error", "No transaction selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        String selectedType = transactionTypeComboBox.getValue();
        String query = "";
        switch (selectedType) {
            case "Purchases":
                query = "DELETE FROM PurchaseAgreements WHERE purchase_id = ?";
                break;
            case "Rentals":
                query = "DELETE FROM RentalAgreements WHERE rental_id = ?";
                break;
            case "Payments":
                query = "DELETE FROM Payments WHERE payment_id = ?";
                break;
            default:
                showAlert("Error", "Invalid transaction type selected!", Alert.AlertType.ERROR);
                return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, selectedTransaction.getTransactionId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Transaction deleted successfully!", Alert.AlertType.INFORMATION);
                loadTransactions(); // Refresh the table
            } else {
                showAlert("Error", "Failed to delete transaction.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while deleting the transaction.", Alert.AlertType.ERROR);
        }
    }



    /**
     * Load all users into the User Table.
     */
    private void loadUsers() {
        userList.clear();
        userList.addAll(adminDAO.getAllUsers());
        userTable.setItems(userList);
    }

    @FXML
    public void searchUser(ActionEvent event) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            loadUsers();
            return;
        }
        userList.clear();
        userList.addAll(adminDAO.searchUsers(query));
        userTable.setItems(userList);
    }

    @FXML
    public void resetTable() {
        searchField.clear();
        loadUsers();
    }

    @FXML
    public void addUser() {
        showAlert("Add User", "This feature is under development.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void editUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "No user selected for editing!", Alert.AlertType.ERROR);
            return;
        }
        showAlert("Edit User", "This feature is under development.", Alert.AlertType.INFORMATION);
    }

    @FXML
    public void deleteUser(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert("Error", "No user selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = adminDAO.deleteUser(selectedUser.getUserId());
        if (success) {
            showAlert("Success", "User deleted successfully!", Alert.AlertType.INFORMATION);
            loadUsers();
        } else {
            showAlert("Error", "Failed to delete user.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Load all listings into the Listing Table.
     */
    public void loadListings() {
        // Clear the current list
        listingList.clear();
        // Fetch all listings from the database
        listingList.addAll(listingDAO.getAllListings());
        // Set the items in the table view
        listingTable.setItems(listingList);
    }

    //#searchListing
    @FXML
    public void searchListing() {
        if (listingSearchField == null) {
            System.out.println("listingSearchField is null!");
            return;
        }
        String listingType = listingSearchField.getText().trim();
        if (listingType.isEmpty()) {
            showAlert("Error", "Please enter a listing type to search.", Alert.AlertType.ERROR);
            return;
        }
        listingList.clear();
        listingList.addAll(listingDAO.searchListings(listingType));
        listingTable.setItems(listingList);
    }


    @FXML
    public void resetListingTable() {
        // Clear the search text field
        listingSearchField.clear();
        // Load all listings from the database
        loadListings();
    }
    @FXML
    public void deleteListing() {
        Listing selectedListing = listingTable.getSelectionModel().getSelectedItem();
        if (selectedListing == null) {
            showAlert("Error", "No listing selected for deletion!", Alert.AlertType.ERROR);
            return;
        }

        boolean success = listingDAO.deleteListing(selectedListing.getListingId());
        if (success) {
            showAlert("Success", "Listing deleted successfully!", Alert.AlertType.INFORMATION);
            loadListings();
        } else {
            showAlert("Error", "Failed to delete listing.", Alert.AlertType.ERROR);
        }
    }
    


    private void hideAllPanes() {
        userManagementPane.setVisible(false);
        listingManagementPane.setVisible(false);
        transactionManagementPane.setVisible(false);
        reportsPane.setVisible(false);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

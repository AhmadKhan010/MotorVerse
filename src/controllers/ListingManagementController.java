package controllers;

import dao.ListingDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Listing;

public class ListingManagementController {

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
    private TextField searchField;

    private ObservableList<Listing> listingList = FXCollections.observableArrayList();
    private ListingDAO listingDAO = new ListingDAO();

    @FXML
    public void initialize() {
        // Initialize columns
        colListingId.setCellValueFactory(new PropertyValueFactory<>("listingId"));
        colSellerId.setCellValueFactory(new PropertyValueFactory<>("sellerId"));
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colListingType.setCellValueFactory(new PropertyValueFactory<>("listingType"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colRentalPrice.setCellValueFactory(new PropertyValueFactory<>("rentalPrice"));

        loadListings();
    }

    @FXML
    public void loadListings() {
        listingList.clear();
        listingList.addAll(listingDAO.getAllListings());
        listingTable.setItems(listingList);
    }

    @FXML
    public void searchListing() {
        String listingType = searchField.getText();
        if (listingType.isEmpty()) {
            showAlert("Error", "Please enter a listing type to search.", Alert.AlertType.ERROR);
            return;
        }
        listingList.clear();
        listingList.addAll(listingDAO.searchListings(listingType));
        listingTable.setItems(listingList);
    }

    @FXML
    public void resetTable() {
        searchField.clear();
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

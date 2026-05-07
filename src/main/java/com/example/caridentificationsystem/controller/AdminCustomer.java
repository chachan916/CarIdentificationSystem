package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.CustomerEnquiry;
import com.example.caridentificationsystem.util.DataManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminCustomer implements Initializable {

    // --- Stats Labels ---
    @FXML private Label totalCustomer, openQueries, resolvedQueries, vehiclesOwned;

    // --- Filter Bar Components ---
    @FXML private Button allCustomers;
    @FXML private ComboBox<String> sortDropdown;

    // --- Customer Records Tab ---
    @FXML private TableView<CustomerEnquiry> customerTable;
    @FXML private TableColumn<CustomerEnquiry, String> colName, colPhone, colStatus, colId, colLastQuery;
    @FXML private TableColumn<CustomerEnquiry, Integer> colVehicles, colQueries;
    @FXML private Pagination pagination;

    // --- Customer Queries Tab ---
    @FXML private TableView<CustomerEnquiry> tblQueries;
    @FXML private TableColumn<CustomerEnquiry, String> colQCustName, colQReg, colQDate, colQText, colQStatus;
    @FXML private TextField txtSearchQuery;
    @FXML private ComboBox<String> cmbQueryType;
    @FXML private Button allCustomerQueries, openQueries1, urgentQuery, respondedQueries;

    // --- Bottom Feed ---
    @FXML private ListView<String> recentQueryList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTables();
        setupComboBoxes();
        setupPagination();

        // Connect both TableViews to the shared data
        if (customerTable != null) customerTable.setItems(DataManager.getSharedEnquiries());
        if (tblQueries != null) tblQueries.setItems(DataManager.getSharedEnquiries());

        updateStats();
        loadMockData();
    }

    private void setupTables() {
        // --- Customer Records Tab Mapping ---
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        if (colPhone != null) colPhone.setCellValueFactory(new PropertyValueFactory<>("ownerContact"));
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        if (colStatus != null) colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Mapping new columns introduced in FXML
        if (colVehicles != null) colVehicles.setCellValueFactory(new PropertyValueFactory<>("condition")); // Using condition as placeholder or update model
        if (colQueries != null) colQueries.setCellValueFactory(new PropertyValueFactory<>("queryType"));
        if (colLastQuery != null) colLastQuery.setCellValueFactory(new PropertyValueFactory<>("date"));

        // --- Customer Queries Tab Mapping ---
        if (colQCustName != null) colQCustName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        if (colQReg != null) colQReg.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        if (colQDate != null) colQDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        if (colQText != null) colQText.setCellValueFactory(new PropertyValueFactory<>("notes"));
        if (colQStatus != null) colQStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void updateStats() {
        if (DataManager.getSharedEnquiries() == null) return;

        int total = DataManager.getSharedEnquiries().size();
        long open = DataManager.getSharedEnquiries().stream()
                .filter(q -> q.getStatus() != null && "Open".equalsIgnoreCase(q.getStatus()))
                .count();
        long resolved = DataManager.getSharedEnquiries().stream()
                .filter(q -> q.getStatus() != null && "Resolved".equalsIgnoreCase(q.getStatus()))
                .count();

        if (totalCustomer != null) totalCustomer.setText(String.valueOf(total));
        if (openQueries != null) openQueries.setText(String.valueOf(open));
        if (resolvedQueries != null) resolvedQueries.setText(String.valueOf(resolved));
        if (vehiclesOwned != null) vehiclesOwned.setText(String.valueOf(total * 2)); // Mock multiplier
    }

    private void setupComboBoxes() {
        if (sortDropdown != null) sortDropdown.setItems(FXCollections.observableArrayList("Name (A-Z)", "Recent Activity"));
        if (cmbQueryType != null) cmbQueryType.setItems(FXCollections.observableArrayList("Technical", "Maintenance", "Insurance"));
    }

    private void setupPagination() {
        if (pagination != null) {
            pagination.setPageCount(6);
            pagination.setPageFactory(pageIndex -> new Label("Showing Page " + (pageIndex + 1)));
        }
    }

    private void loadMockData() {
        if (recentQueryList != null) {
            recentQueryList.getItems().clear();
            recentQueryList.getItems().add("System: Database synced with Customer Portal");
            recentQueryList.getItems().add("Notice: Statistics updated automatically");
        }
    }

    // --- Sidebar Navigation ---
    @FXML private void handleDashboard(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml"); }
    @FXML private void handleVehicles(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminVehicles.fxml"); }
    @FXML private void handleUsers(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminUsers.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }
    @FXML private void handleWorkshop(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminWorkshop.fxml"); }
    @FXML private void handleInsurance(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminInsurance.fxml"); }
    @FXML private void handlePolice(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml"); }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation error: Could not load " + fxmlPath);
            e.printStackTrace();
        }
    }
}
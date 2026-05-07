package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

public class UserWorkshop implements Initializable {


    @FXML private Label txtUser;
    @FXML private Label txtUser1;


    @FXML private TextField txtVehicleId;
    @FXML private TextField txtMake;
    @FXML private TextField txtModel;
    @FXML private TextField txtYear;
    @FXML private TextField txtOwnerId;


    @FXML private TextField txtRegNo;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> comboServiceType;
    @FXML private ComboBox<String> comboServiceStatus;
    @FXML private TextField txtTechnicianName;
    @FXML private TextField txtCost;
    @FXML private DatePicker datePickerCompletion;
    @FXML private TextArea txtServiceDetails;
    @FXML private TextField txtSearch;

    // --- TableView Components ---
    @FXML private TableView<WorkshopRecord> workshopTable;
    @FXML private TableColumn<WorkshopRecord, String> colVehicleId;
    @FXML private TableColumn<WorkshopRecord, String> colMake;
    @FXML private TableColumn<WorkshopRecord, String> colModel;
    @FXML private TableColumn<WorkshopRecord, String> colYear;
    @FXML private TableColumn<WorkshopRecord, String> colOwnerId;
    @FXML private TableColumn<WorkshopRecord, String> colRegNo;
    @FXML private TableColumn<WorkshopRecord, String> colTechnician;
    @FXML private TableColumn<WorkshopRecord, String> colServiceType;
    @FXML private TableColumn<WorkshopRecord, String> colService;
    @FXML private TableColumn<WorkshopRecord, String> colCost;
    @FXML private TableColumn<WorkshopRecord, String> colDate;
    @FXML private TableColumn<WorkshopRecord, String> colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        loadComboBoxes();
        displaySessionUser();
    }

    private void displaySessionUser() {
        User sessionUser = Login.getLoggedInUser();
        if (sessionUser != null) {
            String id = sessionUser.getUserId();
            if (txtUser != null) txtUser.setText(id);
            if (txtUser1 != null) txtUser1.setText(id);
        } else {
            if (txtUser != null) txtUser.setText("Guest");
            if (txtUser1 != null) txtUser1.setText("Guest User");
        }
    }

    private void setupTable() {
        // Mapping columns
        colVehicleId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colTechnician.setCellValueFactory(new PropertyValueFactory<>("technician"));
        colServiceType.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        colService.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        workshopTable.setItems(AdminWorkshop.getGlobalWorkshopData());
    }

    private void loadComboBoxes() {
        comboServiceType.setItems(FXCollections.observableArrayList("General Service", "Engine Repair", "Brake System", "Electrical", "Body Work"));
        comboServiceStatus.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Completed", "On Hold"));
    }

    // --- ACTION HANDLERS ---

    @FXML
    private void handleAddRecord(ActionEvent event) {
        if (txtRegNo.getText().isEmpty() || txtVehicleId.getText().isEmpty() || txtTechnicianName.getText().isEmpty()) {
            showAlert("Input Error", "Vehicle ID, Registration Number, and Technician are required.");
            return;
        }

        WorkshopRecord record = new WorkshopRecord(
                txtVehicleId.getText(),
                txtMake.getText(),
                txtModel.getText(),
                txtYear.getText(),
                txtOwnerId.getText(),
                txtRegNo.getText(),
                txtTechnicianName.getText(),
                comboServiceType.getValue() != null ? comboServiceType.getValue() : "N/A",
                txtServiceDetails.getText(),
                txtCost.getText(),
                datePicker.getValue() != null ? datePicker.getValue().toString() : "",
                comboServiceStatus.getValue() != null ? comboServiceStatus.getValue() : "Pending"
        );

        // Adding to the shared global list so Admin can see it
        AdminWorkshop.getGlobalWorkshopData().add(record);
        handleClearForm(null);
        showAlert("Success", "Record added and synced with Admin dashboard.");
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        txtVehicleId.clear();
        txtMake.clear();
        txtModel.clear();
        txtYear.clear();
        txtOwnerId.clear();
        txtRegNo.clear();
        txtTechnicianName.clear();
        txtCost.clear();
        txtServiceDetails.clear();
        datePicker.setValue(null);
        datePickerCompletion.setValue(null);
        comboServiceType.getSelectionModel().clearSelection();
        comboServiceStatus.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = txtSearch.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            workshopTable.setItems(AdminWorkshop.getGlobalWorkshopData());
            return;
        }

        ObservableList<WorkshopRecord> filteredList = FXCollections.observableArrayList();
        for (WorkshopRecord record : AdminWorkshop.getGlobalWorkshopData()) {
            if (record.getRegNo().toLowerCase().contains(query) ||
                    record.getVehicleId().toLowerCase().contains(query) ||
                    record.getMake().toLowerCase().contains(query) ||
                    record.getStatus().toLowerCase().contains(query)) {
                filteredList.add(record);
            }
        }
        workshopTable.setItems(filteredList);
    }

    @FXML
    private void handleDeleteRecord(ActionEvent event) {
        WorkshopRecord selected = workshopTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            AdminWorkshop.getGlobalWorkshopData().remove(selected);
        } else {
            showAlert("Selection Required", "Please select a record from the table to delete.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/caridentificationsystem/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static class WorkshopRecord {
        private final String vehicleId, make, model, year, ownerId;
        private final String regNo, technician, serviceType, description, cost, date, status;

        public WorkshopRecord(String vehicleId, String make, String model, String year, String ownerId,
                              String regNo, String technician, String serviceType, String description,
                              String cost, String date, String status) {
            this.vehicleId = vehicleId;
            this.make = make;
            this.model = model;
            this.year = year;
            this.ownerId = ownerId;
            this.regNo = regNo;
            this.technician = technician;
            this.serviceType = serviceType;
            this.description = description;
            this.cost = cost;
            this.date = date;
            this.status = status;
        }

        public String getVehicleId() { return vehicleId; }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public String getYear() { return year; }
        public String getOwnerId() { return ownerId; }
        public String getRegNo() { return regNo; }
        public String getTechnician() { return technician; }
        public String getServiceType() { return serviceType; }
        public String getDescription() { return description; }
        public String getCost() { return cost; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }
}
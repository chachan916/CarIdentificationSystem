package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.controller.UserWorkshop.WorkshopRecord;
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
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminWorkshop implements Initializable {


    private static final ObservableList<WorkshopRecord> globalWorkshopData = FXCollections.observableArrayList();

    public static ObservableList<WorkshopRecord> getGlobalWorkshopData() {
        return globalWorkshopData;
    }


    @FXML private Label lblTotalVehicles;
    @FXML private Label lblServiceRecords;
    @FXML private Label lblTotalServiceCost;


    @FXML private TableView<WorkshopRecord> tblServiceRecords;
    @FXML private TableColumn<WorkshopRecord, String> colSrvId, colSrvVehId, colSrvDate, colSrvType, colSrvDesc, colSrvCost;


    @FXML private TableView<WorkshopRecord> vehicleTable;
    @FXML private TableColumn<WorkshopRecord, String> colVehId, colVehMake, colVehModel, colVehYear, colVehReg, colVehOwner;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupServiceRecordsTable();
        setupVehicleTable();


        if (tblServiceRecords != null) tblServiceRecords.setItems(globalWorkshopData);
        if (vehicleTable != null) vehicleTable.setItems(globalWorkshopData);


        updateDashboardStats();


        globalWorkshopData.addListener((javafx.collections.ListChangeListener<WorkshopRecord>) c -> updateDashboardStats());
    }

    private void updateDashboardStats() {
        // 1. Total Unique Vehicles based on Vehicle ID
        long totalVehicles = globalWorkshopData.stream()
                .map(WorkshopRecord::getVehicleId)
                .filter(Objects::nonNull)
                .distinct()
                .count();
        if (lblTotalVehicles != null) lblTotalVehicles.setText(String.valueOf(totalVehicles));

        // 2. Total Service Records
        if (lblServiceRecords != null) lblServiceRecords.setText(String.valueOf(globalWorkshopData.size()));

        // 3. Total Service Cost (Parsing the string cost to double)
        double totalCost = globalWorkshopData.stream()
                .mapToDouble(r -> {
                    try {
                        return Double.parseDouble(r.getCost().replaceAll("[^0-9.]", ""));
                    } catch (Exception e) {
                        return 0.0;
                    }
                }).sum();
        if (lblTotalServiceCost != null) lblTotalServiceCost.setText("M " + String.format("%.2f", totalCost));
    }

    private void setupServiceRecordsTable() {
        if (colSrvId != null) colSrvId.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        if (colSrvVehId != null) colSrvVehId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        if (colSrvDate != null) colSrvDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        if (colSrvType != null) colSrvType.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        if (colSrvDesc != null) colSrvDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        if (colSrvCost != null) colSrvCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    private void setupVehicleTable() {

        if (colVehId != null) colVehId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        if (colVehMake != null) colVehMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        if (colVehModel != null) colVehModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        if (colVehYear != null) colVehYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        if (colVehReg != null) colVehReg.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        if (colVehOwner != null) colVehOwner.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
    }


    @FXML private void handleDashboard(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml"); }
    @FXML private void handleVehicles(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminVehicles.fxml"); }
    @FXML private void handleUsers(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminUsers.fxml"); }
    @FXML private void handleInsurance(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminInsurance.fxml"); }
    @FXML private void handleCustomer(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminCustomer.fxml"); }
    @FXML private void handlePolice(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation Error: " + e.getMessage());
        }
    }
}
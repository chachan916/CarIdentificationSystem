package com.example.caridentificationsystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;



public class Admin implements Initializable {

    @FXML private PieChart moduleUsage;
    @FXML private BarChart<String, Number> vehicleBarChat;
    @FXML private LineChart<String, Number> userAccountChart;

    @FXML private Label lblTotalVehicles, lblActiveUsers, lblBlacklisted;

    @FXML private TableView<VehicleModel> mainVehicleTable;
    @FXML private TableColumn<VehicleModel, String> colRegNo, colModel, colOwner, colStatus, colModule;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupPieChart();
        setupBarChart();
        setupLineChart();
        updateDashboardStats();
    }

    private void updateDashboardStats() {
        lblTotalVehicles.setText("2");
        lblBlacklisted.setText("1");


        try {
            long activeCount = AdminUsers.getUsersList().stream()
                    .filter(user -> user.getStatus().equalsIgnoreCase("Active"))
                    .count();
            lblActiveUsers.setText(String.valueOf(activeCount));
        } catch (Exception e) {
            lblActiveUsers.setText("0");
        }
    }



    @FXML
    private void handleWorkshop(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminWorkshop.fxml");
    }

    @FXML
    private void handleInsurance(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminInsurance.fxml");
    }

    @FXML
    private void handlePolice(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml");
    }

    @FXML
    private void handleCustomer(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminCustomer.fxml");
    }

    @FXML
    private void handleVehicles(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminVehicles.fxml");
    }

    @FXML
    private void handleUsers(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/AdminUsers.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml");
    }

    @FXML
    private void handleAddVehicle(ActionEvent event) {
        handleVehicles(event);
    }


    private void navigateTo(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Navigation error: Could not load " + path);
            e.printStackTrace();
        }
    }

    // --- DATA SETUP ---

    private void setupTable() {
        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colModule.setCellValueFactory(new PropertyValueFactory<>("module"));

        ObservableList<VehicleModel> data = FXCollections.observableArrayList(
                new VehicleModel("T-8821", "Toyota Hilux", "teboho", "Active", "Police"),
                new VehicleModel("B-4412", "Mercedes C200", "tsepo", "Active", "Insurance")
        );
        mainVehicleTable.setItems(data);
    }

    private void setupPieChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Police", 50),
                new PieChart.Data("Insurance", 20),
                new PieChart.Data("Workshop", 30)
        );
        moduleUsage.setData(pieChartData);
    }

    private void setupBarChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Vehicle Activity 2026");
        series.getData().add(new XYChart.Data<>("Apr", 110));
        vehicleBarChat.getData().add(series);
    }

    private void setupLineChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("System Activity");
        series.getData().add(new XYChart.Data<>("Jan", 20));
        series.getData().add(new XYChart.Data<>("Feb", 35));
        series.getData().add(new XYChart.Data<>("Mar", 45));
        series.getData().add(new XYChart.Data<>("Apr", 60));
        userAccountChart.getData().add(series);
    }


    public static class VehicleModel {
        private final String regNo, model, owner, status, module;
        public VehicleModel(String regNo, String model, String owner, String status, String module) {
            this.regNo = regNo; this.model = model; this.owner = owner; this.status = status; this.module = module;
        }
        public String getRegNo() { return regNo; }
        public String getModel() { return model; }
        public String getOwner() { return owner; }
        public String getStatus() { return status; }
        public String getModule() { return module; }
    }
}
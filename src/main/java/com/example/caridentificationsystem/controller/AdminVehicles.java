package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminVehicles implements Initializable {

    @FXML private Label lblTotalVehicles, lblActiveVehicles, lblBlacklistedVehicles;
    @FXML private TableView<VehicleModel> vehicleTable;
    @FXML private TableColumn<VehicleModel, String> colRegNo, colMake, colModel, colOwner, colModule, colColor, colStatus;
    @FXML private TableColumn<VehicleModel, Integer> colYear;

    private ObservableList<VehicleModel> vehicleList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        loadVehicleData();
    }

    private void setupTableColumns() {
        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        colModule.setCellValueFactory(new PropertyValueFactory<>("module"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadVehicleData() {
        vehicleList.clear();
        String sql = "SELECT * FROM vehicles ORDER BY reg_no ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                vehicleList.add(new VehicleModel(
                        rs.getString("reg_no"), rs.getString("make"),
                        rs.getString("model"), rs.getInt("year"),
                        rs.getString("owner"), rs.getString("module"),
                        rs.getString("color"), rs.getString("status")
                ));
            }
            vehicleTable.setItems(vehicleList);
            updateStats();
        } catch (SQLException e) {
            showAlert("Database Error", "Could not load data: " + e.getMessage());
        }
    }

    private void updateStats() {
        lblTotalVehicles.setText(String.valueOf(vehicleList.size()));
        lblActiveVehicles.setText(String.valueOf(vehicleList.stream().filter(v -> v.getStatus().equalsIgnoreCase("Active")).count()));
        lblBlacklistedVehicles.setText(String.valueOf(vehicleList.stream().filter(v -> v.getStatus().equalsIgnoreCase("Blacklisted")).count()));
    }

    @FXML
    private void handleAddVehicle(ActionEvent event) {
        showVehicleDialog(null);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        VehicleModel selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showVehicleDialog(selected);
        } else {
            showAlert("Selection Required", "Please select a vehicle to edit.");
        }
    }

    private void showVehicleDialog(VehicleModel existing) {
        Dialog<VehicleModel> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add New Vehicle" : "Edit Vehicle");
        ButtonType saveBtn = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20, 150, 10, 10));

        TextField reg = new TextField();
        TextField make = new TextField();
        TextField model = new TextField();
        TextField year = new TextField();
        TextField owner = new TextField();
        ComboBox<String> module = new ComboBox<>(FXCollections.observableArrayList("Police", "Insurance", "Workshop"));
        TextField color = new TextField();
        ComboBox<String> status = new ComboBox<>(FXCollections.observableArrayList("Active", "Blacklisted"));

        if (existing != null) {
            reg.setText(existing.getRegNo()); reg.setDisable(true);
            make.setText(existing.getMake());
            model.setText(existing.getModel());
            year.setText(String.valueOf(existing.getYear()));
            owner.setText(existing.getOwner());
            module.setValue(existing.getModule());
            color.setText(existing.getColor());
            status.setValue(existing.getStatus());
        }

        grid.add(new Label("Reg No:"), 0, 0); grid.add(reg, 1, 0);
        grid.add(new Label("Make:"), 0, 1); grid.add(make, 1, 1);
        grid.add(new Label("Model:"), 0, 2); grid.add(model, 1, 2);
        grid.add(new Label("Year:"), 0, 3); grid.add(year, 1, 3);
        grid.add(new Label("Owner:"), 0, 4); grid.add(owner, 1, 4);
        grid.add(new Label("Module:"), 0, 5); grid.add(module, 1, 5);
        grid.add(new Label("Color:"), 0, 6); grid.add(color, 1, 6);
        grid.add(new Label("Status:"), 0, 7); grid.add(status, 1, 7);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn == saveBtn) {
                String sql = (existing == null)
                        ? "INSERT INTO vehicles (reg_no, make, model, year, owner, module, color, status) VALUES (?,?,?,?,?,?,?,?)"
                        : "UPDATE vehicles SET make=?, model=?, year=?, owner=?, module=?, color=?, status=? WHERE reg_no=?";

                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    if (existing == null) {
                        pstmt.setString(1, reg.getText());
                        pstmt.setString(2, make.getText());
                        pstmt.setString(3, model.getText());
                        pstmt.setInt(4, Integer.parseInt(year.getText()));
                        pstmt.setString(5, owner.getText());
                        pstmt.setString(6, module.getValue());
                        pstmt.setString(7, color.getText());
                        pstmt.setString(8, status.getValue());
                    } else {
                        pstmt.setString(1, make.getText());
                        pstmt.setString(2, model.getText());
                        pstmt.setInt(3, Integer.parseInt(year.getText()));
                        pstmt.setString(4, owner.getText());
                        pstmt.setString(5, module.getValue());
                        pstmt.setString(6, color.getText());
                        pstmt.setString(7, status.getValue());
                        pstmt.setString(8, existing.getRegNo());
                    }
                    pstmt.executeUpdate();
                    loadVehicleData();
                } catch (Exception e) { showAlert("Error", "Action failed: " + e.getMessage()); }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        VehicleModel selected = vehicleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selected.getRegNo() + "?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait().ifPresent(res -> {
                if (res == ButtonType.YES) {
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM vehicles WHERE reg_no = ?")) {
                        pstmt.setString(1, selected.getRegNo());
                        pstmt.executeUpdate();
                        loadVehicleData();
                    } catch (SQLException e) { showAlert("Error", e.getMessage()); }
                }
            });
        }
    }

    @FXML private void handleRefresh() { loadVehicleData(); }
    @FXML private void handleDashboard(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml"); }
    @FXML private void handleInsurance(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminInsurance.fxml"); }
    @FXML private void handlePolice(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml"); }
    @FXML private void handleWorkshop(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminWorkshop.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }

    private void navigateTo(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Model class used across all controllers
    public static class VehicleModel {
        private String regNo, make, model, owner, module, color, status;
        private int year;

        public VehicleModel(String regNo, String make, String model, int year, String owner, String module, String color, String status) {
            this.regNo = regNo; this.make = make; this.model = model; this.year = year;
            this.owner = owner; this.module = module; this.color = color; this.status = status;
        }

        public String getRegNo() { return regNo; }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public int getYear() { return year; }
        public String getOwner() { return owner; }
        public String getModule() { return module; }
        public String getColor() { return color; }
        public String getStatus() { return status; }
    }
}
package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.util.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminPolice implements Initializable {
    @FXML private TableView<AdminVehicles.VehicleModel> policeTable;
    @FXML private TableColumn<AdminVehicles.VehicleModel, String> colReg, colMake, colModel, colOwner, colStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colReg.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        loadData();
    }

    private void loadData() {
        ObservableList<AdminVehicles.VehicleModel> list = FXCollections.observableArrayList();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM vehicles WHERE module = 'Police'")) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new AdminVehicles.VehicleModel(rs.getString("reg_no"), rs.getString("make"), rs.getString("model"),
                        rs.getInt("year"), rs.getString("owner"), rs.getString("module"), rs.getString("color"), rs.getString("status")));
            }
            policeTable.setItems(list);
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/caridentificationsystem/view/AdminVehicles.fxml"));
            ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(new Scene(root));
        } catch (Exception e) { e.printStackTrace(); }
    }
}
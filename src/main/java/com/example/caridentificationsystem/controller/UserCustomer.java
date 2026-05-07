package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.CustomerEnquiry;
import com.example.caridentificationsystem.util.DataManager;
import javafx.collections.transformation.FilteredList;
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

public class UserCustomer implements Initializable {

    @FXML private Label txtUser;
    @FXML private TextField txtOwnerName, txtOwnerContact, txtOwnerEmail, txtRegNo, txtSearch;
    @FXML private ComboBox<String> comboQueryType, comboVehicleCondition, comboQueryStatus;
    @FXML private DatePicker datePickerQuery;
    @FXML private TextArea txtQueryNotes;

    @FXML private TableView<CustomerEnquiry> customerTable;
    @FXML private TableColumn<CustomerEnquiry, String> colOwnerName, colContact, colEmail, colRegNo,
            colCondition, colQueryType, colDate, colStatus, colNotes;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();


        customerTable.setItems(DataManager.getSharedEnquiries());
    }

    private void setupComboBoxes() {
        comboQueryType.getItems().addAll("Maintenance", "Registration", "Accident Report", "General Inquiry");
        comboVehicleCondition.getItems().addAll("New", "Used", "Damaged", "Stolen");
        comboQueryStatus.getItems().addAll("Open", "In Progress", "Closed");
    }

    private void setupTableColumns() {
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));


        colContact.setCellValueFactory(new PropertyValueFactory<>("ownerContact"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("ownerEmail"));

        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colCondition.setCellValueFactory(new PropertyValueFactory<>("condition"));
        colQueryType.setCellValueFactory(new PropertyValueFactory<>("queryType"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("notes"));
    }

    @FXML
    private void handleAddQuery(ActionEvent event) {
        if (txtOwnerName.getText().isEmpty() || txtRegNo.getText().isEmpty()) {
            showAlert("Input Error", "Please provide Owner Name and Registration Number.");
            return;
        }


        CustomerEnquiry newEntry = new CustomerEnquiry(
                txtOwnerName.getText(),
                txtOwnerContact.getText(), // Corrected parameter usage
                txtOwnerEmail.getText(),   // Corrected parameter usage
                txtRegNo.getText(),
                comboVehicleCondition.getValue() != null ? comboVehicleCondition.getValue() : "N/A",
                comboQueryType.getValue() != null ? comboQueryType.getValue() : "General",
                datePickerQuery.getValue() != null ? datePickerQuery.getValue().toString() : "N/A",
                comboQueryStatus.getValue() != null ? comboQueryStatus.getValue() : "Open",
                txtQueryNotes.getText()
        );

        // Add to the SHARED manager
        DataManager.getSharedEnquiries().add(newEntry);
        handleClearForm(null);
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchText = txtSearch.getText().toLowerCase();

        if (searchText == null || searchText.isEmpty()) {
            customerTable.setItems(DataManager.getSharedEnquiries());
            return;
        }

        FilteredList<CustomerEnquiry> filteredData = new FilteredList<>(DataManager.getSharedEnquiries(), p -> {
            return p.getOwnerName().toLowerCase().contains(searchText) ||
                    p.getRegNo().toLowerCase().contains(searchText);
        });

        customerTable.setItems(filteredData);
    }

    @FXML
    private void handleDeleteQuery(ActionEvent event) {
        CustomerEnquiry selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            DataManager.getSharedEnquiries().remove(selected);
        } else {
            showAlert("Selection Error", "Please select a record from the table to delete.");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        txtOwnerName.clear();
        txtOwnerContact.clear();
        txtOwnerEmail.clear();
        txtRegNo.clear();
        txtQueryNotes.clear();
        txtSearch.clear();
        comboQueryType.setValue(null);
        comboVehicleCondition.setValue(null);
        comboQueryStatus.setValue(null);
        datePickerQuery.setValue(null);

        customerTable.setItems(DataManager.getSharedEnquiries());
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/caridentificationsystem/view/Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
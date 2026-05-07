package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.PoliceDataStore;
import com.example.caridentificationsystem.model.PoliceReport;
import com.example.caridentificationsystem.model.FlaggedVehicle;
import javafx.collections.FXCollections;
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

public class UserPolice implements Initializable {


    @FXML private Label txtUser;
    @FXML private TextField txtRegNo, txtOwnerName, txtOfficerBadge, txtOfficerName, txtLocation, txtSearch;
    @FXML private TextField txtMake, txtModel, txtYear, txtColour;
    @FXML private ComboBox<String> comboIncidentType, comboVehicleStatus;
    @FXML private DatePicker datePickerIncident;

    // TableView and Columns ---
    @FXML private TableView<PoliceReport> policeTable;
    @FXML private TableColumn<PoliceReport, String> colRegNo, colOwnerName, colMake, colModel, colYear, colColour;
    @FXML private TableColumn<PoliceReport, String> colBadgeNo, colOfficerName, colIncidentType, colDate, colNotes, colVehicleStatus;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComboBoxes();
        setupTableColumns();

        // Link TableView to the centralized DataStore
        policeTable.setItems(PoliceDataStore.getReports());

        if (txtUser != null) {
            txtUser.setText("OFFICER TERMINAL");
        }
    }

    private void setupComboBoxes() {
        comboIncidentType.setItems(FXCollections.observableArrayList(
                "Speeding", "Accident", "Theft Report", "Expired License", "Routine Check"
        ));
        comboVehicleStatus.setItems(FXCollections.observableArrayList(
                "Impounded", "Cleared", "Stolen", "Under Investigation", "Warning Issued"
        ));
    }

    private void setupTableColumns() {

        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("owner"));
        colMake.setCellValueFactory(new PropertyValueFactory<>("make"));
        colModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colColour.setCellValueFactory(new PropertyValueFactory<>("colour"));
        colBadgeNo.setCellValueFactory(new PropertyValueFactory<>("badgeNo"));
        colOfficerName.setCellValueFactory(new PropertyValueFactory<>("officer"));
        colIncidentType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colNotes.setCellValueFactory(new PropertyValueFactory<>("location"));


        colVehicleStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    public void handleSearch(ActionEvent event) {
        String query = txtSearch.getText().toLowerCase();

        FilteredList<PoliceReport> filteredData = new FilteredList<>(PoliceDataStore.getReports(), record -> {
            if (query == null || query.isEmpty()) {
                return true;
            }
            return record.getRegNo().toLowerCase().contains(query) ||
                    record.getBadgeNo().toLowerCase().contains(query);
        });

        policeTable.setItems(filteredData);
    }

    @FXML
    public void handleLogIncident(ActionEvent event) {
        if (isFormValid()) {

            PoliceReport report = new PoliceReport(
                    txtRegNo.getText(),
                    txtOwnerName.getText(),
                    comboIncidentType.getValue(),
                    (datePickerIncident.getValue() != null) ? datePickerIncident.getValue().toString() : "",
                    txtOfficerName.getText(),
                    txtMake.getText(),
                    txtModel.getText(),
                    txtYear.getText(),
                    txtColour.getText(),
                    txtOfficerBadge.getText(),
                    txtLocation.getText(),
                    comboVehicleStatus.getValue()
            );

            // Maintain the FlaggedVehicle logic for the Admin dashboard
            FlaggedVehicle flagged = new FlaggedVehicle(
                    txtRegNo.getText(), txtMake.getText(), txtModel.getText(),
                    txtYear.getText(), txtColour.getText(), txtOwnerName.getText(),
                    comboIncidentType.getValue(), comboVehicleStatus.getValue(), txtOfficerName.getText()
            );

            // Update shared DataStore
            PoliceDataStore.addReport(report);
            PoliceDataStore.addFlagged(flagged);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Incident Logged Successfully.");
            handleClearForm(null);
        } else {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Registration Number and Incident Type are required.");
        }
    }

    @FXML
    public void handleClearForm(ActionEvent event) {
        txtRegNo.clear();
        txtOwnerName.clear();
        txtMake.clear();
        txtModel.clear();
        txtYear.clear();
        txtColour.clear();
        txtOfficerBadge.clear();
        txtOfficerName.clear();
        txtLocation.clear();
        txtSearch.clear();
        comboIncidentType.setValue(null);
        comboVehicleStatus.setValue(null);
        datePickerIncident.setValue(null);

        // Reset table view
        policeTable.setItems(PoliceDataStore.getReports());
    }

    @FXML
    public void handleDeleteRecord(ActionEvent event) {
        PoliceReport selected = policeTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            PoliceDataStore.getReports().remove(selected);
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/caridentificationsystem/view/Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isFormValid() {
        return !txtRegNo.getText().isEmpty() && comboIncidentType.getValue() != null;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class UserInsurance implements Initializable {


    private static final ObservableList<PolicyRecord> globalInsuranceData = FXCollections.observableArrayList();

    public static ObservableList<PolicyRecord> getGlobalInsuranceData() {
        return globalInsuranceData;
    }


    @FXML private Label txtUser;
    @FXML private Label txtUser1;

    @FXML private TextField txtRegNo, txtOwnerName, txtPolicyNumber, txtInsurerName, txtPremiumAmount, txtSearch, txtMakeModel;
    @FXML private ComboBox<String> comboCoverType;
    @FXML private DatePicker datePickerStart, datePickerExpiry;
    @FXML private TextArea txtPolicyNotes;

    @FXML private TableView<PolicyRecord> insuranceTable;
    @FXML private TableColumn<PolicyRecord, String> colRegNo, colOwnerName, colPolicyNumber, colInsurer, colCoverType, colStartDate, colExpiryDate, colPremium, colStatus, colMakeModel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displaySessionUser();

        comboCoverType.setItems(FXCollections.observableArrayList(
                "Third Party",
                "Full Comprehensive",
                "Fire and Theft"
        ));

        setupTableColumns();
        insuranceTable.setItems(globalInsuranceData);
    }

    private void displaySessionUser() {
        User sessionUser = Login.getLoggedInUser();
        if (sessionUser != null) {
            String userId = sessionUser.getUserId();
            if (txtUser != null) txtUser.setText(userId);
            if (txtUser1 != null) txtUser1.setText(userId);
        } else {
            if (txtUser != null) txtUser.setText("Guest");
            if (txtUser1 != null) txtUser1.setText("Guest User");
        }
    }

    private void setupTableColumns() {
        colRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colOwnerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        colPolicyNumber.setCellValueFactory(new PropertyValueFactory<>("policyNumber"));
        colInsurer.setCellValueFactory(new PropertyValueFactory<>("insurer"));
        colCoverType.setCellValueFactory(new PropertyValueFactory<>("coverType"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colExpiryDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        colPremium.setCellValueFactory(new PropertyValueFactory<>("premium"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        // New column mapping
        colMakeModel.setCellValueFactory(new PropertyValueFactory<>("makeModel"));
    }

    @FXML
    private void handleAddPolicy(ActionEvent event) {
        if (isInputValid()) {
            LocalDate today = LocalDate.now();
            LocalDate expiry = datePickerExpiry.getValue();
            String status = today.isAfter(expiry) ? "Expired" : "Active";


            PolicyRecord newPolicy = new PolicyRecord(
                    txtRegNo.getText(),
                    txtOwnerName.getText(),
                    txtPolicyNumber.getText(),
                    txtInsurerName.getText(),
                    comboCoverType.getValue(),
                    datePickerStart.getValue().toString(),
                    expiry.toString(),
                    "M " + txtPremiumAmount.getText(),
                    status,
                    txtMakeModel.getText() // New Field
            );

            globalInsuranceData.add(newPolicy);
            handleClearForm(null);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Insurance Policy for " + newPolicy.getRegNo() + " added successfully.");
        }
    }

    @FXML
    private void handleClearForm(ActionEvent event) {
        txtRegNo.clear();
        txtOwnerName.clear();
        txtPolicyNumber.clear();
        txtInsurerName.clear();
        txtPremiumAmount.clear();
        txtPolicyNotes.clear();
        txtMakeModel.clear();
        comboCoverType.getSelectionModel().clearSelection();
        datePickerStart.setValue(null);
        datePickerExpiry.setValue(null);
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = txtSearch.getText().toLowerCase();
        if (query.isEmpty()) {
            insuranceTable.setItems(globalInsuranceData);
            return;
        }

        FilteredList<PolicyRecord> filtered = new FilteredList<>(globalInsuranceData, p ->
                p.getRegNo().toLowerCase().contains(query) ||
                        p.getPolicyNumber().toLowerCase().contains(query) ||
                        p.getMakeModel().toLowerCase().contains(query)
        );
        insuranceTable.setItems(filtered);
    }

    @FXML
    private void handleDeletePolicy(ActionEvent event) {
        PolicyRecord selected = insuranceTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            globalInsuranceData.remove(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a policy record from the table to delete.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/caridentificationsystem/view/Login.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Logout Navigation Failed: " + e.getMessage());
        }
    }

    private boolean isInputValid() {
        if (txtRegNo.getText().isEmpty() || txtPolicyNumber.getText().isEmpty() ||
                comboCoverType.getValue() == null || datePickerExpiry.getValue() == null || txtMakeModel.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill required fields: Reg No, Policy No, Cover Type, Expiry Date, and Make/Model.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static class PolicyRecord {
        private final String regNo, ownerName, policyNumber, insurer, coverType, startDate, expiryDate, premium, status, makeModel;

        public PolicyRecord(String regNo, String ownerName, String policyNumber, String insurer, String coverType, String startDate, String expiryDate, String premium, String status, String makeModel) {
            this.regNo = regNo;
            this.ownerName = ownerName;
            this.policyNumber = policyNumber;
            this.insurer = insurer;
            this.coverType = coverType;
            this.startDate = startDate;
            this.expiryDate = expiryDate;
            this.premium = premium;
            this.status = status;
            this.makeModel = makeModel;
        }

        public String getRegNo() { return regNo; }
        public String getOwnerName() { return ownerName; }
        public String getPolicyNumber() { return policyNumber; }
        public String getInsurer() { return insurer; }
        public String getCoverType() { return coverType; }
        public String getStartDate() { return startDate; }
        public String getExpiryDate() { return expiryDate; }
        public String getPremium() { return premium; }
        public String getStatus() { return status; }
        public String getMakeModel() { return makeModel; }
    }
}
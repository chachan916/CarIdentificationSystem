package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.controller.UserInsurance.PolicyRecord;
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

public class AdminInsurance implements Initializable {


    @FXML private Label totalPolicies;
    @FXML private Label totalActive;
    @FXML private Label expiringSoon;
    @FXML private Label totalExpired;

    @FXML private ProgressBar progressComp;
    @FXML private ProgressBar progressThirdParty;
    @FXML private ProgressBar progressFireTheft;


    @FXML private Label displayComprehensive;
    @FXML private Label displayThirdParty;
    @FXML private Label displayFireTheft;


    @FXML private TableView<PolicyRecord> tblAllPolicies;
    @FXML private TableColumn<PolicyRecord, String> colPolNum;
    @FXML private TableColumn<PolicyRecord, String> colPolReg;
    @FXML private TableColumn<PolicyRecord, String> colPolOwner;
    @FXML private TableColumn<PolicyRecord, String> colPolMakeModel;
    @FXML private TableColumn<PolicyRecord, String> colPolType;
    @FXML private TableColumn<PolicyRecord, String> colPolInsurer;
    @FXML private TableColumn<PolicyRecord, String> colPolStart;
    @FXML private TableColumn<PolicyRecord, String> colPolExpiry;
    @FXML private TableColumn<PolicyRecord, String> colPolPremium;
    @FXML private TableColumn<PolicyRecord, String> colPolStatus;

    @FXML private Pagination pagination;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();


        ObservableList<PolicyRecord> sharedData = UserInsurance.getGlobalInsuranceData();

        if (tblAllPolicies != null) {
            tblAllPolicies.setItems(sharedData);
        }


        updateDashboardStats(sharedData);
    }

    private void updateDashboardStats(ObservableList<PolicyRecord> data) {
        if (data == null || data.isEmpty()) {
            resetStats();
            return;
        }

        int active = 0, soon = 0, expired = 0;
        int compCount = 0, thirdCount = 0, fireCount = 0;
        double total = data.size();

        for (PolicyRecord p : data) {

            String status = (p.getStatus() != null) ? p.getStatus().toLowerCase() : "";
            if (status.contains("active")) active++;
            else if (status.contains("soon")) soon++;
            else if (status.contains("expired")) expired++;


            String type = (p.getCoverType() != null) ? p.getCoverType().toLowerCase() : "";
            if (type.contains("comprehensive")) compCount++;
            else if (type.contains("third party")) thirdCount++;
            else if (type.contains("fire")) fireCount++;
        }


        totalPolicies.setText(String.valueOf((int) total));
        totalActive.setText(String.valueOf(active));
        expiringSoon.setText(String.valueOf(soon));
        totalExpired.setText(String.valueOf(expired));


        if (progressComp != null) progressComp.setProgress(compCount / total);
        if (progressThirdParty != null) progressThirdParty.setProgress(thirdCount / total);
        if (progressFireTheft != null) progressFireTheft.setProgress(fireCount / total);


        if (displayComprehensive != null) displayComprehensive.setText(String.valueOf(compCount));
        if (displayThirdParty != null) displayThirdParty.setText(String.valueOf(thirdCount));
        if (displayFireTheft != null) displayFireTheft.setText(String.valueOf(fireCount));
    }

    private void resetStats() {
        totalPolicies.setText("0"); totalActive.setText("0");
        expiringSoon.setText("0"); totalExpired.setText("0");
        displayComprehensive.setText("0"); displayThirdParty.setText("0");
        displayFireTheft.setText("0");
    }

    private void setupTableColumns() {
        colPolNum.setCellValueFactory(new PropertyValueFactory<>("policyNumber"));
        colPolReg.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        colPolOwner.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        colPolMakeModel.setCellValueFactory(new PropertyValueFactory<>("makeModel"));
        colPolType.setCellValueFactory(new PropertyValueFactory<>("coverType"));
        colPolInsurer.setCellValueFactory(new PropertyValueFactory<>("insurer"));
        colPolStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colPolExpiry.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        colPolPremium.setCellValueFactory(new PropertyValueFactory<>("premium"));
        colPolStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    // --- NAVIGATION HANDLERS ---

    @FXML private void handleDashboard(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml"); }
    @FXML private void handleVehicles(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminVehicles.fxml"); }
    @FXML private void handleUsers(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminUsers.fxml"); }
    @FXML private void handleWorkshop(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminWorkshop.fxml"); }
    @FXML private void handlePolice(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml"); }
    @FXML private void handleCustomer(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminCustomer.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }

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
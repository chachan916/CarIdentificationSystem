package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AdminUsers {


    private static final ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colUserId, colNames, colEmail, colPassword, colRole, colStatus;
    @FXML private Label lblTotalUsers, lblActiveUsers, lblSuspendedUsers;
    @FXML private TextField txtFullNames, txtEmail, txtGeneratedId;
    @FXML private PasswordField txtPassword;
    @FXML private MenuButton menuRole;

    private String selectedRole = "Select Role";



    public static ObservableList<User> getUsersList() {
        return usersList;
    }


    public static void addUser(User user) {
        usersList.add(user);
    }

    @FXML
    public void initialize() {

        colUserId.setCellValueFactory(cellData -> cellData.getValue().userIdProperty());
        colNames.setCellValueFactory(cellData -> cellData.getValue().namesProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        colRole.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        colStatus.setCellValueFactory(cellData -> cellData.getValue().statusProperty());


        for (MenuItem item : menuRole.getItems()) {
            item.setOnAction(e -> {
                selectedRole = item.getText();
                menuRole.setText(selectedRole);
            });
        }

        userTable.setItems(usersList);


        if (usersList.isEmpty()) {
            usersList.add(new User("Adm-0001", "System Admin", "admin@gmail.com", "Admin1234", "Admin", "Active"));
        }

        updateStats();
    }

    @FXML
    private void handleCreateUser(ActionEvent event) {
        if (validateInput()) {
            User newUser = new User(txtGeneratedId.getText(), txtFullNames.getText(),
                    txtEmail.getText(), txtPassword.getText(),
                    selectedRole, "Active");
            usersList.add(newUser);
            updateStats();
            clearFields();
        }
    }

    @FXML
    private void handleGenerateUserId(ActionEvent event) {
        String fullName = txtFullNames.getText().trim();
        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Required", "Please enter names first.");
            return;
        }
        String prefix = fullName.split(" ")[0].toLowerCase();
        // Generate ID based on current list size
        txtGeneratedId.setText(prefix + String.format("%04d", usersList.size() + 1));
    }

    private void updateStats() {
        lblTotalUsers.setText(String.valueOf(usersList.size()));
        long active = usersList.stream().filter(u -> u.getStatus().equalsIgnoreCase("Active")).count();
        long suspended = usersList.stream().filter(u -> u.getStatus().equalsIgnoreCase("Suspended")).count();
        lblActiveUsers.setText(String.valueOf(active));
        lblSuspendedUsers.setText(String.valueOf(suspended));
    }

    @FXML private void handleEdit(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtFullNames.setText(selected.getNames());
            txtEmail.setText(selected.getEmail());
            txtGeneratedId.setText(selected.getUserId());
            txtPassword.setText(selected.getPassword());
            menuRole.setText(selected.getRole());
            selectedRole = selected.getRole();
        }
    }

    @FXML private void handleDelete(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            usersList.remove(selected);
            updateStats();
        }
    }

    @FXML private void handleSuspend(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Suspended");
            userTable.refresh();
            updateStats();
        }
    }

    @FXML private void handleUnsuspend(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setStatus("Active");
            userTable.refresh();
            updateStats();
        }
    }

    @FXML private void handleRefresh(ActionEvent event) { userTable.refresh(); updateStats(); }
    @FXML private void handleCancel(ActionEvent event) { clearFields(); }

    // --- Navigation ---
    @FXML private void handleDashboard(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml"); }
    @FXML private void handleVehicles(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminVehicles.fxml"); }
    @FXML private void handleLogout(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }
    @FXML private void handleInsurance(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminInsurance.fxml"); }
    @FXML private void handlePolice(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminPolice.fxml"); }
    @FXML private void handleCustomer(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminCustomer.fxml"); }
    @FXML private void handleWorkshop(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/AdminWorkshop.fxml"); }

    private void navigateTo(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(path)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load: " + path);
        }
    }

    private boolean validateInput() {
        return !txtFullNames.getText().isEmpty() && !txtGeneratedId.getText().isEmpty() && !selectedRole.equals("Select Role");
    }

    private void clearFields() {
        txtFullNames.clear(); txtEmail.clear(); txtPassword.clear();
        txtGeneratedId.clear(); menuRole.setText("Select Role"); selectedRole = "Select Role";
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
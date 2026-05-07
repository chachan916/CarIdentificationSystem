package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.ResourceBundle;

public class SignUp implements Initializable {

    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUserId;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;


    @FXML private RadioButton rbCustomer;
    @FXML private RadioButton rbWorkshop;
    @FXML private RadioButton rbInsurance;
    @FXML private RadioButton rbPolice;

    private ToggleGroup moduleGroup;

    private static final String ID_STORAGE_FILE = "user_count.txt";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        moduleGroup = new ToggleGroup();
        rbCustomer.setToggleGroup(moduleGroup);
        rbWorkshop.setToggleGroup(moduleGroup);
        rbInsurance.setToggleGroup(moduleGroup);
        rbPolice.setToggleGroup(moduleGroup);

        // Set a default selection
        rbCustomer.setSelected(true);
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        if (validateInput()) {

            RadioButton selectedRadioButton = (RadioButton) moduleGroup.getSelectedToggle();
            String selectedRole = selectedRadioButton.getText();


            User newUser = new User(
                    txtUserId.getText(),
                    txtFullName.getText(),
                    txtEmail.getText(),
                    txtPassword.getText(),
                    selectedRole,
                    "Active"
            );

            // 2. Add to the shared list in AdminUsers
            AdminUsers.addUser(newUser);

            // 3. Increment counter and navigate
            incrementStoredId();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration complete as " + selectedRole + "!");
            navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml");
        }
    }

    private boolean validateInput() {
        if (txtFullName.getText().isEmpty() || txtUserId.getText().isEmpty() || txtEmail.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields and generate an ID.");
            return false;
        }
        if (moduleGroup.getSelectedToggle() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a User Module.");
            return false;
        }
        if (!txtPassword.getText().equals(txtConfirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return false;
        }
        if (txtPassword.getText().length() < 4) {
            showAlert(Alert.AlertType.ERROR, "Error", "Password too short.");
            return false;
        }
        return true;
    }


    @FXML
    private void handleGetuserid(ActionEvent event) {
        String fullName = txtFullName.getText().trim();
        if (fullName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter your full name before generating an ID.");
            return;
        }
        String firstName = fullName.split(" ")[0].toLowerCase();
        int nextId = getNextIdNumber();
        String generatedId = firstName + String.format("%04d", nextId);
        if (txtUserId != null) {
            txtUserId.setText(generatedId);
            txtUserId.setEditable(false);
        }
    }

    private int getNextIdNumber() {
        try {
            if (!Files.exists(Paths.get(ID_STORAGE_FILE))) return 1;
            BufferedReader reader = new BufferedReader(new FileReader(ID_STORAGE_FILE));
            String line = reader.readLine();
            reader.close();
            return (line != null) ? Integer.parseInt(line.trim()) : 1;
        } catch (Exception e) { return 1; }
    }

    private void incrementStoredId() {
        int currentId = getNextIdNumber();
        try (PrintWriter out = new PrintWriter(new FileWriter(ID_STORAGE_FILE))) {
            out.println(currentId + 1);
        } catch (IOException e) { e.printStackTrace(); }
    }


    @FXML private void handleConfirm(ActionEvent event) { handleSignUp(event); }
    @FXML private void handleLogin(ActionEvent event) { navigateTo(event, "/com/example/caridentificationsystem/view/Login.fxml"); }
    @FXML private void handleBackToLogin(ActionEvent event) { handleLogin(event); }

    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
package com.example.caridentificationsystem.controller;

import com.example.caridentificationsystem.model.User;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML private TextField userId;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private MenuButton userType;
    @FXML private ImageView logo;
    @FXML private Button loginButton;


    private static User loggedInUser;

    public static User getLoggedInUser() {
        return loggedInUser;
    }


    private final String ADMIN_ID = "Adm-0001";
    private final String ADMIN_EMAIL = "admin@gmail.com";
    private final String ADMIN_PASS = "Admin1234";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        applyHeartbeatEffect(logo);
        applyFadeEffect(loginButton);
        setupTimedShake(userId);
        setupTimedShake(emailField);
        setupTimedShake(passwordField);

        for (MenuItem item : userType.getItems()) {
            item.setOnAction(e -> userType.setText(item.getText()));
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String id = userId.getText().trim();
        String email = emailField.getText().trim();
        String pass = passwordField.getText();
        String type = userType.getText();

        if (id.isEmpty() || email.isEmpty() || pass.isEmpty() || type.equals("Type")) {
            showAlert(Alert.AlertType.WARNING, "Login Error", "Please fill all fields and select a User Type.");
            return;
        }

        // 1. Check for Admin first
        if (type.equalsIgnoreCase("Admin")) {
            if (id.equals(ADMIN_ID) && email.equals(ADMIN_EMAIL) && pass.equals(ADMIN_PASS)) {
                // Set admin as the logged-in user (optional, creates a dummy User object)
                loggedInUser = new User(ADMIN_ID, "System Admin", ADMIN_EMAIL, ADMIN_PASS, "Admin", "Active");
                navigateTo(event, "/com/example/caridentificationsystem/view/Admin.fxml");
                return;
            }
        }


        boolean authenticated = false;
        for (User user : AdminUsers.getUsersList()) {
            if (user.getUserId().equals(id) &&
                    user.getEmail().equals(email) &&
                    user.getPassword().equals(pass) &&
                    user.getRole().equalsIgnoreCase(type)) {


                loggedInUser = user;
                authenticated = true;
                routeByRole(event, user.getRole());
                break;
            }
        }

        if (!authenticated) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "Invalid credentials or role selection.");
        }
    }

    /**
     * Determines which FXML dashboard to load based on the user's role
     */
    private void routeByRole(ActionEvent event, String role) {
        String path;
        switch (role.toLowerCase()) {
            case "workshop":
                path = "/com/example/caridentificationsystem/view/UserWorkshop.fxml";
                break;
            case "insurance":
                path = "/com/example/caridentificationsystem/view/UserInsurance.fxml";
                break;
            case "police":
                path = "/com/example/caridentificationsystem/view/UserPolice.fxml";
                break;
            case "customer":
                path = "/com/example/caridentificationsystem/view/UserCustomer.fxml";
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Error", "Unknown User Role.");
                return;
        }
        navigateTo(event, path);
    }

    @FXML
    private void handleSignup(ActionEvent event) {
        navigateTo(event, "/com/example/caridentificationsystem/view/SignUp.fxml");
    }

    // --- Helpers ---

    private void navigateTo(ActionEvent event, String path) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Could not load: " + path);
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void setupTimedShake(TextField field) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), field);
        shake.setByX(5); shake.setCycleCount(10); shake.setAutoReverse(true);
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        SequentialTransition seq = new SequentialTransition(shake, pause);
        seq.setCycleCount(Animation.INDEFINITE);
        seq.play();
        field.textProperty().addListener((obs, old, val) -> {
            if (!val.isEmpty()) { seq.stop(); field.setTranslateX(0); } else { seq.play(); }
        });
    }

    private void applyHeartbeatEffect(ImageView target) {
        ScaleTransition st = new ScaleTransition(Duration.millis(1000), target);
        st.setByX(0.1); st.setByY(0.1); st.setCycleCount(Animation.INDEFINITE);
        st.setAutoReverse(true); st.play();
    }

    private void applyFadeEffect(Button target) {
        FadeTransition ft = new FadeTransition(Duration.millis(1500), target);
        ft.setFromValue(1.0); ft.setToValue(0.5);
        ft.setCycleCount(Animation.INDEFINITE);
        ft.setAutoReverse(true); ft.play();
    }
}
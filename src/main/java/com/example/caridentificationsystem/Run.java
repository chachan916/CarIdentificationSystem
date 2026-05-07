package com.example.caridentificationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class Run extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        URL fxmlLocation = getClass().getResource("/com/example/caridentificationsystem/view/Login.fxml");

        if (fxmlLocation == null) {
            throw new IOException("Error: Could not find Login.fxml. Check if it is in src/main/resources/com/example/caridentificationsystem/view/");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        Scene scene = new Scene(fxmlLoader.load(), 1038, 719);
        stage.setTitle("Vehicle Identification System - Login");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
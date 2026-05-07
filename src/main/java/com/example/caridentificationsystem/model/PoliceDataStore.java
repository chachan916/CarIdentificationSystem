package com.example.caridentificationsystem.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PoliceDataStore {

    private static final ObservableList<PoliceReport> allReports = FXCollections.observableArrayList();
    private static final ObservableList<FlaggedVehicle> allFlaggedVehicles = FXCollections.observableArrayList();

    public static ObservableList<PoliceReport> getReports() {
        return allReports;
    }

    public static ObservableList<FlaggedVehicle> getFlaggedVehicles() {
        return allFlaggedVehicles;
    }

    public static void addReport(PoliceReport report) {
        allReports.add(report);
    }

    public static void addFlagged(FlaggedVehicle vehicle) {
        allFlaggedVehicles.add(vehicle);
    }
}
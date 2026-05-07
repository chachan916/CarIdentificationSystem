package com.example.caridentificationsystem.util;

import com.example.caridentificationsystem.model.CustomerEnquiry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataManager {
    private static final ObservableList<CustomerEnquiry> sharedEnquiries = FXCollections.observableArrayList();

    public static ObservableList<CustomerEnquiry> getSharedEnquiries() {
        return sharedEnquiries;
    }
}
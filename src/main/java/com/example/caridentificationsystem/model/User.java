package com.example.caridentificationsystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    private final StringProperty userId;
    private final StringProperty names;
    private final StringProperty email;
    private final StringProperty password;
    private final StringProperty role;
    private final StringProperty status;

    public User(String userId, String names, String email, String password, String role, String status) {
        this.userId = new SimpleStringProperty(userId);
        this.names = new SimpleStringProperty(names);
        this.email = new SimpleStringProperty(email);
        this.password = new SimpleStringProperty(password);
        this.role = new SimpleStringProperty(role);
        this.status = new SimpleStringProperty(status);
    }


    public StringProperty userIdProperty() { return userId; }
    public StringProperty namesProperty() { return names; }
    public StringProperty emailProperty() { return email; }
    public StringProperty passwordProperty() { return password; }
    public StringProperty roleProperty() { return role; }
    public StringProperty statusProperty() { return status; }


    public String getUserId() { return userId.get(); }
    public String getNames() { return names.get(); }
    public String getEmail() { return email.get(); }
    public String getPassword() { return password.get(); }
    public String getRole() { return role.get(); }
    public String getStatus() { return status.get(); }

    public void setStatus(String value) { this.status.set(value); }
}
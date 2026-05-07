package com.example.caridentificationsystem.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CustomerEnquiry {
    private final StringProperty ownerName;
    private final StringProperty ownerContact;
    private final StringProperty ownerEmail;
    private final StringProperty regNo;
    private final StringProperty condition;
    private final StringProperty queryType;
    private final StringProperty date;
    private final StringProperty status;
    private final StringProperty notes;

    public CustomerEnquiry(String ownerName, String ownerContact, String ownerEmail, String regNo,
                           String condition, String queryType, String date, String status, String notes) {
        this.ownerName = new SimpleStringProperty(ownerName);
        this.ownerContact = new SimpleStringProperty(ownerContact);
        this.ownerEmail = new SimpleStringProperty(ownerEmail);
        this.regNo = new SimpleStringProperty(regNo);
        this.condition = new SimpleStringProperty(condition);
        this.queryType = new SimpleStringProperty(queryType);
        this.date = new SimpleStringProperty(date);
        this.status = new SimpleStringProperty(status);
        this.notes = new SimpleStringProperty(notes);
    }


    public StringProperty ownerNameProperty() { return ownerName; }
    public StringProperty ownerContactProperty() { return ownerContact; }
    public StringProperty ownerEmailProperty() { return ownerEmail; }
    public StringProperty regNoProperty() { return regNo; }
    public StringProperty statusProperty() { return status; }



    public String getOwnerName() { return ownerName.get(); }


    public String getOwnerContact() { return ownerContact.get(); }


    public String getOwnerEmail() { return ownerEmail.get(); }

    public String getRegNo() { return regNo.get(); }

    public String getCondition() { return condition.get(); }

    public String getQueryType() { return queryType.get(); }

    public String getDate() { return date.get(); }

    public String getStatus() { return status.get(); }

    public String getNotes() { return notes.get(); }


    public void setStatus(String status) { this.status.set(status); }
}
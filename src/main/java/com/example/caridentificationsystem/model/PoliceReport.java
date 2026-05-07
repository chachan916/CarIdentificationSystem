package com.example.caridentificationsystem.model;

/**
 * Model representing a detailed police incident report.
 * Used to populate TableViews in both User and Admin modules.
 */
public class PoliceReport {
    private final String regNo;
    private final String owner;
    private final String type;
    private final String date;
    private final String officer;
    private final String make;
    private final String model;
    private final String year;
    private final String colour;
    private final String badgeNo;
    private final String location;
    private final String status;

    public PoliceReport(String regNo, String owner, String type, String date, String officer,
                        String make, String model, String year, String colour,
                        String badgeNo, String location, String status) {
        this.regNo = regNo;
        this.owner = owner;
        this.type = type;
        this.date = date;
        this.officer = officer;
        this.make = make;
        this.model = model;
        this.year = year;
        this.colour = colour;
        this.badgeNo = badgeNo;
        this.location = location;
        this.status = status;
    }


    public String getRegNo() { return regNo; }
    public String getOwner() { return owner; }
    public String getType() { return type; }
    public String getDate() { return date; }
    public String getOfficer() { return officer; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getYear() { return year; }
    public String getColour() { return colour; }
    public String getBadgeNo() { return badgeNo; }
    public String getLocation() { return location; }
    public String getStatus() { return status; } // New getter for Vehicle Status
}
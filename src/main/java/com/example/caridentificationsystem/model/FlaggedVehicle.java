package com.example.caridentificationsystem.model;

public class FlaggedVehicle {
    private String regNo;
    private String make;
    private String model;
    private String year;
    private String colour;
    private String owner;
    private String type;
    private String status;
    private String officer;

    public FlaggedVehicle(String regNo, String make, String model, String year,
                          String colour, String owner, String type,
                          String status, String officer) {
        this.regNo = regNo;
        this.make = make;
        this.model = model;
        this.year = year;
        this.colour = colour;
        this.owner = owner;
        this.type = type;
        this.status = status;
        this.officer = officer;
    }



    public String getRegNo() { return regNo; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public String getYear() { return year; }
    public String getColour() { return colour; }
    public String getOwner() { return owner; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getOfficer() { return officer; }
}
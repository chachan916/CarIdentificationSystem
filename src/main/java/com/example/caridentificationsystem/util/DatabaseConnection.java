package com.example.caridentificationsystem.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Update these credentials to match your pgAdmin setup
    private static final String URL = "jdbc:postgresql://localhost:5432/vehicle_identification_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "505716";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL Driver not found", e);
        }
    }
}
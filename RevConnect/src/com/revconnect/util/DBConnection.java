package com.revconnect.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Database URL, username, password
    private static final String URL = "jdbc:mysql://localhost:3306/revconnect";
    private static final String USER = "root";           // replace with your MySQL username
    private static final String PASS = "123456789";       // replace with your MySQL password

    // Method to get Connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // Main method to test connection
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to database successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
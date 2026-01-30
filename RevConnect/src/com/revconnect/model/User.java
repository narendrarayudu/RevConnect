package com.revconnect.model;

public class User {

    private int userId;
    private String email;
    private String username;
    private String password;
    private String userType;

    // 1️⃣ Empty constructor (Java needs this)
    public User() {}

    // 2️⃣ Constructor used during registration
    public User(String email, String username, String password, String userType) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // 3️⃣ Constructor used during login (DB → Java)
    public User(int userId, String email, String username, String password, String userType) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    // ===== Getters & Setters =====

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

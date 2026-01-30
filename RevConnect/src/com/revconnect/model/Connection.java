package com.revconnect.model;

public class Connection {

    private int connectionId;   // DB primary key
    private int requesterId;    // sender
    private int receiverId;     // receiver
    private String status;      // PENDING / ACCEPTED / REJECTED

    // ✅ No-arg constructor (IMPORTANT for Java beans)
    public Connection() {}

    // ✅ Constructor used while sending request
    public Connection(int requesterId, int receiverId) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.status = "PENDING";
    }

    // ✅ Full constructor (optional, future use)
    public Connection(int connectionId, int requesterId, int receiverId, String status) {
        this.connectionId = connectionId;
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.status = status;
    }

    // ================= GETTERS & SETTERS =================

    public int getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
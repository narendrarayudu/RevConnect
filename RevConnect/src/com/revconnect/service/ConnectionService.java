package com.revconnect.service;

import com.revconnect.dao.ConnectionDAO;
import com.revconnect.model.Connection;

public class ConnectionService {

    private ConnectionDAO connectionDAO = new ConnectionDAO();

    public boolean sendRequest(int userId, int friendId) {
        Connection conn = new Connection(userId, friendId);
        return connectionDAO.sendRequest(conn);
    }
}
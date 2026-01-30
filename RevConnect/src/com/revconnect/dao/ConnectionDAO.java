package com.revconnect.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revconnect.model.Connection;
import com.revconnect.util.DBConnection;

public class ConnectionDAO {

    public boolean sendRequest(Connection connObj) {

        String sql = "INSERT INTO connections(requester_id, receiver_id, status) VALUES (?, ?, ?)";

        try (
            java.sql.Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, connObj.getRequesterId());
            ps.setInt(2, connObj.getReceiverId());
            ps.setString(3, connObj.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
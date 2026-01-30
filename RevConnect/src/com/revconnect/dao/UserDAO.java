package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revconnect.model.User;
import com.revconnect.util.DBConnection;

public class UserDAO {

    public boolean register(User user) throws SQLException {

        String sql = "INSERT INTO users(email, username, password, user_type) VALUES (?, ?, ?, ?)";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, user.getEmail());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getUserType());

        return ps.executeUpdate() > 0;
    }

    public User login(String username, String password) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        Connection con = DBConnection.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("email"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("user_type")
            );
        }

        return null;
    }
}

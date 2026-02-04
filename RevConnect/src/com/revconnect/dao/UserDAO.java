package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revconnect.model.User;
import com.revconnect.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            User user = new User();
            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setUserType(rs.getString("user_type"));
            return user;
        }

        return null;
    }
    
        public boolean updateProfile(User user) {
            String sql = "UPDATE users SET full_name = ?, bio = ?, profile_pic_path = ?, location = ?, website = ? WHERE user_id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, user.getFullName());
                ps.setString(2, user.getBio());
                ps.setString(3, user.getProfilePicPath());
                ps.setString(4, user.getLocation());
                ps.setString(5, user.getWebsite());
                ps.setInt(6, user.getUserId());

                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }

        // Get User by ID
        public User getUserById(int userId) {
            String sql = "SELECT * FROM users WHERE user_id = ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    user.setBio(rs.getString("bio"));
                    user.setProfilePicPath(rs.getString("profile_pic_path"));
                    user.setLocation(rs.getString("location"));
                    user.setWebsite(rs.getString("website"));
                    return user;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Search users by name or username
        public List<User> searchUsers(String keyword) {
            List<User> users = new ArrayList<>();
            String sql = "SELECT user_id, username, full_name FROM users WHERE username LIKE ? OR full_name LIKE ?";
            try (Connection con = DBConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                String search = "%" + keyword + "%";
                ps.setString(1, search);
                ps.setString(2, search);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setFullName(rs.getString("full_name"));
                    users.add(user);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return users;
        }
    
    
}
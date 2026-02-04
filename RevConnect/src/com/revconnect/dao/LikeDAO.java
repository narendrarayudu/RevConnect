package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revconnect.model.Like;
import com.revconnect.util.DBConnection;

public class LikeDAO {

    public boolean addLike(Like like) {
        String sql = "INSERT INTO likes(user_id, post_id) VALUES(?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, like.getUserId());
            ps.setInt(2, like.getPostId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
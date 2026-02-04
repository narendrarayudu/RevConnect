package com.revconnect.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.revconnect.model.Comment;
import com.revconnect.util.DBConnection;

public class CommentDAO {
    public boolean addComment(Comment comment) {
        String sql = "INSERT INTO comments(post_id, user_id, content) VALUES(?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, comment.getPostId());
            ps.setInt(2, comment.getUserId());
            ps.setString(3, comment.getContent());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }
}
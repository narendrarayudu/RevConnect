package com.revconnect.dao;

import com.revconnect.model.Post;
import com.revconnect.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {

    // INSERT post into DB
    public boolean createPost(Post post) {

        String sql = "INSERT INTO posts(user_id, content) VALUES (?, ?)";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public int getPostOwner(int postId) {
        String sql = "SELECT user_id FROM posts WHERE post_id = ?";

        try (
            java.sql.Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // READ all posts
    public List<Post> getAllPosts() {

        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {
                Post p = new Post();
                p.setPostId(rs.getInt("post_id"));
                p.setUserId(rs.getInt("user_id"));
                p.setContent(rs.getString("content"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                posts.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }
}
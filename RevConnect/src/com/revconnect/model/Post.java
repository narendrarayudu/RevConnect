package com.revconnect.model;

import java.sql.Timestamp;

public class Post {

    private int postId;
    private int userId;
    private String content;
    private Timestamp createdAt;

    // empty constructor
    public Post() {}

    // constructor used while creating post
    public Post(int userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

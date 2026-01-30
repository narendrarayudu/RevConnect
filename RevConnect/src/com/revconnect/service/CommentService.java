package com.revconnect.service;

import com.revconnect.dao.CommentDAO;
import com.revconnect.model.Comment;

public class CommentService {
    private CommentDAO commentDAO = new CommentDAO();

    public boolean addComment(int userId, int postId, String content) {
        return commentDAO.addComment(new Comment(postId, userId, content));
    }
}

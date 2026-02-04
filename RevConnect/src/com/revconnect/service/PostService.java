
package com.revconnect.service;

import com.revconnect.dao.PostDAO;
import com.revconnect.model.Post;

import java.util.List;

public class PostService {

    private PostDAO postDAO = new PostDAO();

    public boolean createPost(int userId, String content) {
        Post post = new Post(userId, content);
        return postDAO.createPost(post);
    }

    public List<Post> getFeed() {
        return postDAO.getAllPosts();
    }
    public boolean editPost(int userId, int postId, String content) {
        return postDAO.editPost(userId, postId, content);
    }

    public boolean deletePost(int userId, int postId) {
        return postDAO.deletePost(userId, postId);
    }

    public int getPostOwner(int postId) {
        return postDAO.getPostOwner(postId);
    }
}

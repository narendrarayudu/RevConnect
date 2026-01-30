package com.revconnect.service;

import com.revconnect.dao.LikeDAO;
import com.revconnect.model.Like;

public class LikeService {
    private LikeDAO likeDAO = new LikeDAO();

    public boolean likePost(int userId, int postId) {
        Like like = new Like(userId, postId);
        return likeDAO.addLike(like);
    }
}

package com.revconnect.service;

import java.util.List;
import com.revconnect.dao.NotificationDAO;
import com.revconnect.model.Notification;

public class NotificationService {
    private NotificationDAO notificationDAO = new NotificationDAO();

    public boolean sendNotification(int userId, String message) {
        return notificationDAO.addNotification(new Notification(userId, message));
    }

    public List<Notification> getUserNotifications(int userId) {
        return notificationDAO.getNotifications(userId);
    }
}

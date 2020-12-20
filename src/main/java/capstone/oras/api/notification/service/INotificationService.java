package capstone.oras.api.notification.service;

import capstone.oras.entity.NotificationEntity;

import java.util.List;

public interface INotificationService {
    NotificationEntity createNotification(NotificationEntity notificationEntity);
    NotificationEntity updateNotification(NotificationEntity notificationEntity);
    List<NotificationEntity> updateNotifications(List<NotificationEntity> notificationEntity);
    List<NotificationEntity> getAllNotification();
    List<NotificationEntity> getAllAccountNotification(int accountId);
    List<NotificationEntity> getAllNewAccountNotification(int accountId);
    List<NotificationEntity> getAllNewAccountNotification(int accountId, String role);
    NotificationEntity getNotificationById(int id);
    Integer readNotification(int id);


}

package capstone.oras.api.notification.service;

import capstone.oras.entity.NotificationEntity;
import capstone.oras.model.custom.NotificationModel;

import java.util.List;

public interface INotificationService {
    NotificationEntity createNotification(NotificationEntity notificationEntity);
    NotificationEntity updateNotification(NotificationEntity notificationEntity);
    List<NotificationEntity> updateNotifications(List<NotificationEntity> notificationEntity);
    List<NotificationEntity> getAllNotification();
    List<NotificationEntity> getAllAccountNotification(int accountId);
    List<NotificationEntity> getAllNewAccountNotification(int accountId);
    List<NotificationModel> getAllNewAccountNotification(int accountId, String role);
    NotificationEntity getNotificationById(int id);
    Integer readNotification(Integer id);
    Integer readNotifications(List<Integer> ids);


}

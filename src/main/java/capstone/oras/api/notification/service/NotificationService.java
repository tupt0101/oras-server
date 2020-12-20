package capstone.oras.api.notification.service;

import capstone.oras.dao.INotificationRepository;
import capstone.oras.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    INotificationRepository INotificationRepository;


    @Override
    public NotificationEntity createNotification(NotificationEntity notificationEntity) {
        return INotificationRepository.save(notificationEntity);
    }

    @Override
    public NotificationEntity updateNotification(NotificationEntity notificationEntity) {
        return INotificationRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationEntity> updateNotifications(List<NotificationEntity> notificationEntity) {
        return INotificationRepository.saveAll(notificationEntity);
    }

    @Override
    public List<NotificationEntity> getAllNotification() {
        return INotificationRepository.findAll();
    }

    @Override
    public List<NotificationEntity> getAllAccountNotification(int accountId) {
        if (INotificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).isPresent()) {
            return INotificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public List<NotificationEntity> getAllNewAccountNotification(int accountId) {
        if (INotificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).isPresent()) {
            return INotificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public NotificationEntity getNotificationById(int id) {
        if (INotificationRepository.findById(id).isPresent()) {
            return INotificationRepository.findById(id).get();
        }
        return null;
    }
}

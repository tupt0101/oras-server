package capstone.oras.api.notification.service;

import capstone.oras.dao.INotificationRepository;
import capstone.oras.entity.NotificationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static capstone.oras.common.Constant.AccountRole.ADMIN;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    INotificationRepository notificationRepository;


    @Override
    public NotificationEntity createNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public NotificationEntity updateNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationEntity> updateNotifications(List<NotificationEntity> notificationEntity) {
        return notificationRepository.saveAll(notificationEntity);
    }

    @Override
    public List<NotificationEntity> getAllNotification() {
        return notificationRepository.findAll();
    }

    @Override
    public List<NotificationEntity> getAllAccountNotification(int accountId) {
        if (notificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).isPresent()) {
            return notificationRepository.getNotificationEntitiesByReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public List<NotificationEntity> getAllNewAccountNotification(int accountId) {
        if (notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).isPresent()) {
            return notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).get();
        }
        return null;
    }

    @Override
    public List<NotificationEntity> getAllNewAccountNotification(int accountId, String role) {
        if (ADMIN.equalsIgnoreCase(role)) {
            accountId = 0;
        }
        return notificationRepository.getNotificationEntitiesByNewTrueAndReceiverIdEquals(accountId).orElse(null);
    }

    @Override
    public NotificationEntity getNotificationById(int id) {
        if (notificationRepository.findById(id).isPresent()) {
            return notificationRepository.findById(id).get();
        }
        return null;
    }

    @Override
    public Integer readNotification(int id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification ID not found");
        }
        return notificationRepository.updateIsNew(id, false);
    }
}

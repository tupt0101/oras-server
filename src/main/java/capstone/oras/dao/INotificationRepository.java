package capstone.oras.dao;

import capstone.oras.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface INotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    Optional<List<NotificationEntity>> getNotificationEntitiesByNewTrueAndReceiverIdEquals(int receiverId);
    Optional<List<NotificationEntity>> getNotificationEntitiesByReceiverIdEquals(int receiverId);

}

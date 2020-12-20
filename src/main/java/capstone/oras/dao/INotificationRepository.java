package capstone.oras.dao;

import capstone.oras.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface INotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    Optional<List<NotificationEntity>> getNotificationEntitiesByNewTrueAndReceiverIdEquals(int receiverId);
    Optional<List<NotificationEntity>> getNotificationEntitiesByReceiverIdEquals(int receiverId);
    @Query(value = "update notification set is_new = :isNew where id = :id", nativeQuery = true)
    @Modifying
    @Transactional
    Integer updateIsNew(int id, boolean isNew);

}

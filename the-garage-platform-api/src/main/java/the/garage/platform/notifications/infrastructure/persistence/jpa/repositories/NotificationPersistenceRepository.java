package the.garage.platform.notifications.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;
import the.garage.platform.notifications.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;

import java.util.List;

@Repository
public interface NotificationPersistenceRepository extends JpaRepository<NotificationPersistenceEntity, Long> {
    List<NotificationPersistenceEntity> findByRecipientId(Long recipientId);
    List<NotificationPersistenceEntity> findByStatus(NotificationStatus status);
}
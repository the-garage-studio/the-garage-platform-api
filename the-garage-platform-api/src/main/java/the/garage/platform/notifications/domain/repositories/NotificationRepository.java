package the.garage.platform.notifications.domain.repositories;

import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    Optional<Notification> findById(Long id);
    List<Notification> findByRecipientId(Long recipientId);
    List<Notification> findByStatus(NotificationStatus status);
    Notification save(Notification notification);
}
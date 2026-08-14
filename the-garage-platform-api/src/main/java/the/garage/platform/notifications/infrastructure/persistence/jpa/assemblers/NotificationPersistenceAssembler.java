package the.garage.platform.notifications.infrastructure.persistence.jpa.assemblers;

import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationContent;
import the.garage.platform.notifications.infrastructure.persistence.jpa.entities.NotificationPersistenceEntity;

public final class NotificationPersistenceAssembler {

    private NotificationPersistenceAssembler() {
    }

    public static Notification toDomainFromPersistence(NotificationPersistenceEntity entity) {
        if (entity == null) return null;
        return Notification.reconstitute(
                entity.getId(),
                entity.getRecipientId(),
                entity.getRecipientEmail(),
                new NotificationContent(entity.getSubject(), entity.getBody()),
                entity.getType(),
                entity.getStatus(),
                entity.getSentAt());
    }

    public static NotificationPersistenceEntity toPersistenceFromDomain(Notification notification) {
        if (notification == null) return null;
        var entity = new NotificationPersistenceEntity();
        if (notification.getId() != null) {
            entity.setId(notification.getId());
        }
        entity.setRecipientId(notification.getRecipientId());
        entity.setRecipientEmail(notification.getRecipientEmail());
        entity.setSubject(notification.getContent().subject());
        entity.setBody(notification.getContent().body());
        entity.setType(notification.getType());
        entity.setStatus(notification.getStatus());
        entity.setSentAt(notification.getSentAt());
        return entity;
    }
}
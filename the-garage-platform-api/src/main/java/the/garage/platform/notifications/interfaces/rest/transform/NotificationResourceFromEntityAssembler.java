package the.garage.platform.notifications.interfaces.rest.transform;

import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.interfaces.rest.resources.NotificationResource;

public class NotificationResourceFromEntityAssembler {
    public static NotificationResource toResourceFromEntity(Notification entity) {
        return new NotificationResource(
                entity.getId(),
                entity.getType().name(),
                entity.getContent().subject(),
                entity.getStatus().name(),
                entity.getSentAt() != null ? entity.getSentAt().toString() : null);
    }
}
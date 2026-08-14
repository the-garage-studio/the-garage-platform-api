package the.garage.platform.notifications.application.queries;

import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;

public record GetNotificationsByStatusQuery(NotificationStatus status) {
    public GetNotificationsByStatusQuery {
        if (status == null) throw new IllegalArgumentException("status cannot be null");
    }
}
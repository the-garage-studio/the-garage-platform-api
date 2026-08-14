package the.garage.platform.notifications.application.queryservices;

import the.garage.platform.notifications.application.queries.GetNotificationHistoryQuery;
import the.garage.platform.notifications.application.queries.GetNotificationsByStatusQuery;
import the.garage.platform.notifications.domain.model.aggregates.Notification;

import java.util.List;

public interface NotificationQueryService {
    List<Notification> handle(GetNotificationHistoryQuery query);
    List<Notification> handle(GetNotificationsByStatusQuery query);
}
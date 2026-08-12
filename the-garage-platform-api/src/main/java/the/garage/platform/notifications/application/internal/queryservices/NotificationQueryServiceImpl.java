package the.garage.platform.notifications.application.internal.queryservices;

import org.springframework.stereotype.Service;
import the.garage.platform.notifications.application.queries.GetNotificationHistoryQuery;
import the.garage.platform.notifications.application.queries.GetNotificationsByStatusQuery;
import the.garage.platform.notifications.application.queryservices.NotificationQueryService;
import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.domain.repositories.NotificationRepository;

import java.util.List;

@Service
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> handle(GetNotificationHistoryQuery query) {
        return notificationRepository.findByRecipientId(query.recipientId());
    }

    @Override
    public List<Notification> handle(GetNotificationsByStatusQuery query) {
        return notificationRepository.findByStatus(query.status());
    }
}
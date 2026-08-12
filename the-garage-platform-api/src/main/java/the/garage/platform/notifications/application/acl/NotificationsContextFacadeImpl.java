package the.garage.platform.notifications.application.acl;

import org.springframework.stereotype.Service;
import the.garage.platform.notifications.application.commands.SendNotificationCommand;
import the.garage.platform.notifications.application.commandservices.NotificationCommandService;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationType;
import the.garage.platform.notifications.interfaces.acl.NotificationsContextFacade;

@Service
public class NotificationsContextFacadeImpl implements NotificationsContextFacade {

    private final NotificationCommandService notificationCommandService;

    public NotificationsContextFacadeImpl(NotificationCommandService notificationCommandService) {
        this.notificationCommandService = notificationCommandService;
    }

    @Override
    public boolean sendNotification(Long recipientId, String recipientEmail, String typeName, String subject, String body) {
        try {
            var type = NotificationType.valueOf(typeName);
            var command = new SendNotificationCommand(recipientId, recipientEmail, type, subject, body);
            var result = notificationCommandService.handle(command);
            return result.isSuccess();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
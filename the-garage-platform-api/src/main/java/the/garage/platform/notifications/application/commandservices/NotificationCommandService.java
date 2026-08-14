package the.garage.platform.notifications.application.commandservices;

import the.garage.platform.notifications.application.commands.SendNotificationCommand;
import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

public interface NotificationCommandService {
    Result<Notification, ApplicationError> handle(SendNotificationCommand command);
}
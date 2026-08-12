package the.garage.platform.notifications.application.internal.commandservices;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import the.garage.platform.notifications.application.commands.SendNotificationCommand;
import the.garage.platform.notifications.application.commandservices.NotificationCommandService;
import the.garage.platform.notifications.application.internal.outboundservices.email.EmailSenderPort;
import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.domain.model.valueobjects.EmailAddress;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationContent;
import the.garage.platform.notifications.domain.repositories.NotificationRepository;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

@Service
@Slf4j
public class NotificationCommandServiceImpl implements NotificationCommandService {

    private final NotificationRepository notificationRepository;
    private final EmailSenderPort emailSenderPort;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository, EmailSenderPort emailSenderPort) {
        this.notificationRepository = notificationRepository;
        this.emailSenderPort = emailSenderPort;
    }

    @Override
    @Transactional
    public Result<Notification, ApplicationError> handle(SendNotificationCommand command) {
        try {
            var notification = Notification.create(
                    command.recipientId(),
                    new EmailAddress(command.recipientEmail()),
                    new NotificationContent(command.subject(), command.body()),
                    command.type());
            notification = notificationRepository.save(notification);

            try {
                emailSenderPort.send(command.recipientEmail(), command.subject(), command.body());
                notification.markAsSent();
            } catch (Exception sendException) {
                log.error("Failed to send notification {} to {}: {}",
                        notification.getId(), command.recipientEmail(), sendException.getMessage());
                notification.markAsFailed();
            }

            notification = notificationRepository.save(notification);
            return Result.success(notification);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("Notification", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("send-notification", e.getMessage()));
        }
    }
}
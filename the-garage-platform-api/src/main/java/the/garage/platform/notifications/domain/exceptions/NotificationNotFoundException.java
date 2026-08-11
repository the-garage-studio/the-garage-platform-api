package the.garage.platform.notifications.domain.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long notificationId) {
        super("Notification not found: %s".formatted(notificationId));
    }
}
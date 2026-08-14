package the.garage.platform.notifications.application.commands;

import the.garage.platform.notifications.domain.model.valueobjects.NotificationType;

public record SendNotificationCommand(
        Long recipientId,
        String recipientEmail,
        NotificationType type,
        String subject,
        String body) {

    public SendNotificationCommand {
        if (recipientId == null || recipientId <= 0) {
            throw new IllegalArgumentException("recipientId cannot be null or less than 1");
        }
        if (recipientEmail == null || recipientEmail.isBlank()) {
            throw new IllegalArgumentException("recipientEmail cannot be null or blank");
        }
        if (type == null) {
            throw new IllegalArgumentException("type cannot be null");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject cannot be null or blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body cannot be null or blank");
        }
    }
}
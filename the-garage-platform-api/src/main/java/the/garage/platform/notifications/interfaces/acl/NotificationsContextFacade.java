package the.garage.platform.notifications.interfaces.acl;

public interface NotificationsContextFacade {

    /**
     * Sends a notification and records it for auditing purposes.
     *
     * @param recipientId    the internal id of the recipient (e.g. a User id from Identity & Access)
     * @param recipientEmail the recipient's email address
     * @param typeName       must match a valid {@code NotificationType} name
     * @param subject        email subject
     * @param body           email body
     * @return {@code true} when the notification was recorded successfully (regardless of delivery outcome);
     *         {@code false} only when the command itself was rejected (e.g. invalid data)
     */
    boolean sendNotification(Long recipientId, String recipientEmail, String typeName, String subject, String body);
}
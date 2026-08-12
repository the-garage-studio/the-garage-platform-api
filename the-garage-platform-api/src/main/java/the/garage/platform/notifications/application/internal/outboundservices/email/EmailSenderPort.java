package the.garage.platform.notifications.application.internal.outboundservices.email;

public interface EmailSenderPort {
    void send(String toAddress, String subject, String body);
}
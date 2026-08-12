package the.garage.platform.notifications.infrastructure.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import the.garage.platform.notifications.application.internal.outboundservices.email.EmailSenderPort;

@Component
@Slf4j
@ConditionalOnProperty(
        name = "garage.notifications.provider",
        havingValue = "smtp"
)
public class EmailSenderAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;

    @Value("${garage.notifications.from-address:no-reply@thegarage.dev}")
    private String fromAddress;

    public EmailSenderAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(String toAddress, String subject, String body) {
        var message = new SimpleMailMessage();

        message.setFrom(fromAddress);
        message.setTo(toAddress);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        log.info(
                "Email sent to {} with subject '{}'",
                toAddress,
                subject
        );
    }
}
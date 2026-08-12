package the.garage.platform.notifications.infrastructure.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import the.garage.platform.notifications.application.internal.outboundservices.email.EmailSenderPort;

/**
 * Implementación stub de {@link EmailSenderPort} que solo registra en log.
 *
 * <p>Activa por defecto ({@code matchIfMissing = true}) cuando
 * {@code garage.notifications.provider} no está definido o tiene el valor
 * {@code noop}. Permite que la aplicación arranque correctamente sin necesidad
 * de configurar SMTP, lo que es ideal para el perfil por defecto y para tests.</p>
 */
@Component
@Slf4j
@ConditionalOnProperty(
        name = "garage.notifications.provider",
        havingValue = "noop",
        matchIfMissing = true)
public class NoOpEmailSenderAdapter implements EmailSenderPort {

    @Override
    public void send(String toAddress, String subject, String body) {
        log.info("[STUB] Email would be sent to '{}' with subject '{}'. Body: {}",
                toAddress, subject, body);
    }
}

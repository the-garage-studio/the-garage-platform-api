package the.garage.platform.iam.infrastructure.acl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.internal.outboundservices.acl.NotificationACL;

/**
 * Implementación stub de {@link NotificationACL} que solo registra en log.
 *
 * <p>Activa por defecto ({@code matchIfMissing = true}) para que la aplicación
 * arranque sin configurar SMTP; ideal para entornos de desarrollo y test donde
 * no se requiere envío real de correo.</p>
 */
@Service
@Slf4j
@ConditionalOnProperty(
        name = "garage.notifications.provider",
        havingValue = "noop",
        matchIfMissing = true)
public class NoOpNotificationACL implements NotificationACL {

    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String verificationToken) {
        log.info("[STUB] Verification email would be sent to {} ({}). Token: {}", toEmail, firstName, verificationToken);
    }

    @Override
    public void sendPasswordRecoveryEmail(String toEmail, String firstName, String resetToken) {
        log.info("[STUB] Password recovery email would be sent to {} ({}). Token: {}", toEmail, firstName, resetToken);
    }
}
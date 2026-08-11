package the.garage.platform.iam.infrastructure.acl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.internal.outboundservices.acl.NotificationACL;

/**
 * Implementación real de {@link NotificationACL} que envía correos mediante SMTP
 * a través del {@link JavaMailSender} provisto por Spring Boot Mail Starter.
 *
 * <p>Solo se activa cuando la propiedad {@code garage.notifications.provider=smtp}
 * está definida explícitamente, lo que exige que todas las propiedades
 * {@code spring.mail.*} estén configuradas correctamente en el perfil activo.
 * De lo contrario, el stub {@link NoOpNotificationACL} toma precedencia.</p>
 *
 * <p>Los correos se envían como texto plano (sin HTML) para maximizar la
 * compatibilidad con clientes de correo y simplificar el mantenimiento.</p>
 */
@Service
@Slf4j
@ConditionalOnProperty(
        name = "garage.notifications.provider",
        havingValue = "smtp")
public class SmtpNotificationACL implements NotificationACL {

    private final JavaMailSender mailSender;

    /**
     * Dirección remitente del correo.
     * Se inyecta desde {@code garage.notifications.from-address}; si la propiedad
     * no está definida, se usa {@code no-reply@thegarage.dev} como fallback seguro.
     */
    @Value("${garage.notifications.from-address:no-reply@thegarage.dev}")
    private String fromAddress;

    public SmtpNotificationACL(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía el código de verificación de email al usuario recién registrado.
     *
     * <p>El cuerpo es intencionalmente breve para que el código sea lo primero
     * visible en la vista previa del cliente de correo.</p>
     */
    @Override
    public void sendVerificationEmail(String toEmail, String firstName, String verificationToken) {
        var message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Verifica tu cuenta en The Garage");
        message.setText(
                "Hola, " + firstName + ".\n\n" +
                "Tu código de verificación es: " + verificationToken + "\n" +
                "Expira en 24 horas.\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este mensaje.\n\n" +
                "— El equipo de The Garage"
        );
        try {
            mailSender.send(message);
            log.info("[SMTP] Correo de verificación enviado a {}", toEmail);
        } catch (Exception e) {
            // Se registra el error pero no se relanza para no romper el flujo de registro.
            // El usuario podrá solicitar un nuevo código más adelante.
            log.error("[SMTP] Error al enviar correo de verificación a {}: {}", toEmail, e.getMessage());
        }
    }

    /**
     * Envía el código de restablecimiento de contraseña al usuario.
     *
     * <p>El tiempo de expiración corto (1 hora) se menciona explícitamente en el cuerpo
     * para que el usuario actúe con urgencia.</p>
     */
    @Override
    public void sendPasswordRecoveryEmail(String toEmail, String firstName, String resetToken) {
        var message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(toEmail);
        message.setSubject("Restablece tu contraseña en The Garage");
        message.setText(
                "Hola, " + firstName + ".\n\n" +
                "Tu código de recuperación de contraseña es: " + resetToken + "\n" +
                "Expira en 1 hora.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este mensaje.\n\n" +
                "— El equipo de The Garage"
        );
        try {
            mailSender.send(message);
            log.info("[SMTP] Correo de recuperación enviado a {}", toEmail);
        } catch (Exception e) {
            log.error("[SMTP] Error al enviar correo de recuperación a {}: {}", toEmail, e.getMessage());
        }
    }
}

package the.garage.platform.iam.application.internal.outboundservices.acl;

public interface NotificationACL {
    void sendVerificationEmail(String toEmail, String firstName, String verificationToken);

    void sendPasswordRecoveryEmail(String toEmail, String firstName, String resetToken);
}

package the.garage.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.commands.ConfirmPasswordResetCommand;
import the.garage.platform.iam.application.commands.RequestPasswordResetCommand;
import the.garage.platform.iam.application.commandservices.PasswordResetCommandService;
import the.garage.platform.iam.application.internal.outboundservices.acl.NotificationACL;
import the.garage.platform.iam.application.internal.outboundservices.hashing.HashingService;
import the.garage.platform.iam.domain.model.entities.PasswordResetToken;
import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.model.valueobjects.Password;
import the.garage.platform.iam.domain.repositories.PasswordResetTokenRepository;
import the.garage.platform.iam.domain.repositories.UserRepository;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

import java.time.LocalDateTime;
import the.garage.platform.shared.domain.services.VerificationCodeGenerator;

@Service
public class PasswordResetCommandServiceImpl implements PasswordResetCommandService {

    private static final long RESET_TOKEN_TTL_HOURS = 1;
    private static final String GENERIC_CONFIRMATION_MESSAGE =
            "If an account with that email exists, a password reset link has been sent";

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final HashingService hashingService;
    private final NotificationACL notificationACL;

    public PasswordResetCommandServiceImpl(
            UserRepository userRepository,
            PasswordResetTokenRepository passwordResetTokenRepository,
            HashingService hashingService,
            NotificationACL notificationACL) {
        this.userRepository = userRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.hashingService = hashingService;
        this.notificationACL = notificationACL;
    }

    @Override
    @Transactional
    public Result<String, ApplicationError> handle(RequestPasswordResetCommand command) {
        try {
            var email = new Email(command.email());
            userRepository.findByEmail(email).ifPresent(user -> {
                /*
                 * Se usa VerificationCodeGenerator en lugar de UUID para que el código
                 * sea fácil de escribir a mano; SecureRandom garantiza entropía
                 * suficiente para un token de corta duración (1 hora).
                 */
                var token = VerificationCodeGenerator.generate();
                var expiresAt = LocalDateTime.now().plusHours(RESET_TOKEN_TTL_HOURS);
                passwordResetTokenRepository.save(new PasswordResetToken(user.getId(), token, expiresAt));
                notificationACL.sendPasswordRecoveryEmail(user.getEmail().address(), user.getFirstName(), token);
            });
            return Result.success(GENERIC_CONFIRMATION_MESSAGE);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("email", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(ConfirmPasswordResetCommand command) {
        var resetToken = passwordResetTokenRepository.findByToken(command.token());
        if (resetToken.isEmpty()) {
            return Result.failure(ApplicationError.notFound("PasswordResetToken", command.token()));
        }

        var token = resetToken.get();
        try {
            token.consume();
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("password-reset-token", e.getMessage()));
        }

        var user = userRepository.findById(token.getUserId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", token.getUserId().toString()));
        }

        var updatedUser = user.get();
        updatedUser.changePassword(Password.ofHash(hashingService.encode(command.newRawPassword())));
        userRepository.save(updatedUser);
        passwordResetTokenRepository.save(token);

        return Result.success(updatedUser.getId());
    }
}
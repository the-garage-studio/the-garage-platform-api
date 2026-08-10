package the.garage.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.commands.*;
import the.garage.platform.iam.application.commandservices.UserCommandService;
import the.garage.platform.iam.application.internal.outboundservices.acl.NotificationACL;
import the.garage.platform.iam.application.internal.outboundservices.hashing.HashingService;
import the.garage.platform.iam.application.internal.outboundservices.tokens.TokenService;
import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.model.entities.EmailVerificationToken;
import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.model.valueobjects.Password;
import the.garage.platform.iam.domain.repositories.EmailVerificationTokenRepository;
import the.garage.platform.iam.domain.repositories.UserRepository;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

import java.time.LocalDateTime;
import the.garage.platform.shared.domain.services.VerificationCodeGenerator;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private static final long EMAIL_VERIFICATION_TOKEN_TTL_HOURS = 24;

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final NotificationACL notificationACL;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            EmailVerificationTokenRepository emailVerificationTokenRepository,
            HashingService hashingService,
            TokenService tokenService,
            NotificationACL notificationACL) {
        this.userRepository = userRepository;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.notificationACL = notificationACL;
    }

    @Override
    @Transactional
    public Result<User, ApplicationError> handle(RegisterCollectorCommand command) {
        try {
            var email = new Email(command.email());

            if (userRepository.existsByEmail(email)) {
                return Result.failure(ApplicationError.conflict("User", "Email already registered"));
            }
            if (userRepository.existsByUsername(command.username())) {
                return Result.failure(ApplicationError.conflict("User", "Username already taken"));
            }

            var password = Password.ofHash(hashingService.encode(command.rawPassword()));
            var user = User.register(command.firstName(), command.lastName(), command.username(), email, password);
            var savedUser = userRepository.save(user);

            issueAndSendVerificationToken(savedUser);

            return Result.success(savedUser);
        } catch (IllegalArgumentException e) {
            return Result.failure(ApplicationError.validationError("User", e.getMessage()));
        } catch (Exception e) {
            return Result.failure(ApplicationError.unexpected("register-collector", e.getMessage()));
        }
    }

    @Override
    @Transactional
    public Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command) {
        var user = userRepository.findByUsername(command.username());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", command.username()));
        }
        if (!hashingService.matches(command.password(), user.get().getPassword().hash())) {
            return Result.failure(ApplicationError.validationError("credentials", "Invalid username or password"));
        }
        if (!user.get().isActive()) {
            return Result.failure(ApplicationError.businessRuleViolation("account-status", "Account is not active"));
        }
        var token = tokenService.generateToken(user.get().getUsername());
        return Result.success(ImmutablePair.of(user.get(), token));
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(ChangeUserRoleCommand command) {
        return userRepository.findById(command.userId()).map(user -> {
            user.changeRole(command.newRole());
            return Result.<Long, ApplicationError>success(userRepository.save(user).getId());
        }).orElseGet(() -> Result.failure(ApplicationError.notFound("User", command.userId().toString())));
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(SuspendUserCommand command) {
        return userRepository.findById(command.userId()).map(user -> {
            try {
                user.suspend();
            } catch (IllegalStateException e) {
                return Result.<Long, ApplicationError>failure(ApplicationError.conflict("User", e.getMessage()));
            }
            return Result.<Long, ApplicationError>success(userRepository.save(user).getId());
        }).orElseGet(() -> Result.failure(ApplicationError.notFound("User", command.userId().toString())));
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(ReactivateUserCommand command) {
        return userRepository.findById(command.userId()).map(user -> {
            try {
                user.reactivate();
            } catch (IllegalStateException e) {
                return Result.<Long, ApplicationError>failure(ApplicationError.conflict("User", e.getMessage()));
            }
            return Result.<Long, ApplicationError>success(userRepository.save(user).getId());
        }).orElseGet(() -> Result.failure(ApplicationError.notFound("User", command.userId().toString())));
    }

    /**
     * Genera y persiste un código de verificación de 8 dígitos, luego delega
     * el envío al puerto {@link NotificationACL}.
     *
     * <p>Se usa {@link VerificationCodeGenerator} en lugar de UUID para que el
     * usuario pueda escribir el código a mano en el cliente sin dificultad.</p>
     */
    private void issueAndSendVerificationToken(User user) {
        var token = VerificationCodeGenerator.generate();
        var expiresAt = LocalDateTime.now().plusHours(EMAIL_VERIFICATION_TOKEN_TTL_HOURS);
        var verificationToken = new EmailVerificationToken(user.getId(), token, expiresAt);
        emailVerificationTokenRepository.save(verificationToken);
        notificationACL.sendVerificationEmail(user.getEmail().address(), user.getFirstName(), token);
    }
}
package the.garage.platform.iam.application.internal.commandservices;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.commands.VerifyEmailCommand;
import the.garage.platform.iam.application.commandservices.EmailVerificationCommandService;
import the.garage.platform.iam.domain.repositories.EmailVerificationTokenRepository;
import the.garage.platform.iam.domain.repositories.UserRepository;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

@Service
public class EmailVerificationCommandServiceImpl implements EmailVerificationCommandService {

    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;

    public EmailVerificationCommandServiceImpl(
            EmailVerificationTokenRepository emailVerificationTokenRepository,
            UserRepository userRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(VerifyEmailCommand command) {
        var verificationToken = emailVerificationTokenRepository.findByToken(command.token());
        if (verificationToken.isEmpty()) {
            return Result.failure(ApplicationError.notFound("EmailVerificationToken", command.token()));
        }

        var token = verificationToken.get();
        try {
            token.markVerified();
        } catch (IllegalStateException e) {
            return Result.failure(ApplicationError.businessRuleViolation("email-verification-token", e.getMessage()));
        }

        var user = userRepository.findById(token.getUserId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", token.getUserId().toString()));
        }

        var updatedUser = user.get();
        updatedUser.activate();
        userRepository.save(updatedUser);
        emailVerificationTokenRepository.save(token);

        return Result.success(updatedUser.getId());
    }
}
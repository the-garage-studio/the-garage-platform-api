package the.garage.platform.iam.application.commandservices;

import the.garage.platform.iam.application.commands.VerifyEmailCommand;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

public interface EmailVerificationCommandService {
    Result<Long, ApplicationError> handle(VerifyEmailCommand command);
}

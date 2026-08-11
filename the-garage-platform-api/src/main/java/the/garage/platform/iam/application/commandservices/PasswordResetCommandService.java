package the.garage.platform.iam.application.commandservices;

import the.garage.platform.iam.application.commands.ConfirmPasswordResetCommand;
import the.garage.platform.iam.application.commands.RequestPasswordResetCommand;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

public interface PasswordResetCommandService {
    Result<String, ApplicationError> handle(RequestPasswordResetCommand command);

    Result<Long, ApplicationError> handle(ConfirmPasswordResetCommand command);
}

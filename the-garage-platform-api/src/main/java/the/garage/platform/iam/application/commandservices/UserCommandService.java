package the.garage.platform.iam.application.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import the.garage.platform.iam.application.commands.*;
import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.shared.application.result.ApplicationError;
import the.garage.platform.shared.application.result.Result;

public interface UserCommandService {

    Result<User, ApplicationError> handle(RegisterCollectorCommand command);

    Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);

    Result<Long, ApplicationError> handle(ChangeUserRoleCommand command);

    Result<Long, ApplicationError> handle(SuspendUserCommand command);

    Result<Long, ApplicationError> handle(ReactivateUserCommand command);
}
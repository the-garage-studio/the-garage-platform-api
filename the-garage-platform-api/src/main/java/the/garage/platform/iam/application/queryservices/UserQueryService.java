package the.garage.platform.iam.application.queryservices;

import the.garage.platform.iam.application.queries.GetAllUsersQuery;
import the.garage.platform.iam.application.queries.GetUserByIdQuery;
import the.garage.platform.iam.application.queries.GetUserByUsernameQuery;
import the.garage.platform.iam.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {

    Optional<User> handle(GetUserByIdQuery query);

    Optional<User> handle(GetUserByUsernameQuery query);

    List<User> handle(GetAllUsersQuery query);
}
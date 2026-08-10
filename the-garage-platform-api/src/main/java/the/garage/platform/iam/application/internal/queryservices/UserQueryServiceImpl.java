package the.garage.platform.iam.application.internal.queryservices;

import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.queries.GetAllUsersQuery;
import the.garage.platform.iam.application.queries.GetUserByIdQuery;
import the.garage.platform.iam.application.queries.GetUserByUsernameQuery;
import the.garage.platform.iam.application.queryservices.UserQueryService;
import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery query) {
        return userRepository.findById(query.userId());
    }

    @Override
    public Optional<User> handle(GetUserByUsernameQuery query) {
        return userRepository.findByUsername(query.username());
    }

    @Override
    public List<User> handle(GetAllUsersQuery query) {
        return userRepository.findAll();
    }
}
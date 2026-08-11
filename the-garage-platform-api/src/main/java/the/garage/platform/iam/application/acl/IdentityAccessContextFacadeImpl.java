package the.garage.platform.iam.application.acl;

import org.springframework.stereotype.Service;
import the.garage.platform.iam.application.commands.ChangeUserRoleCommand;
import the.garage.platform.iam.application.commands.ReactivateUserCommand;
import the.garage.platform.iam.application.commands.SuspendUserCommand;
import the.garage.platform.iam.application.commandservices.UserCommandService;
import the.garage.platform.iam.application.queries.GetUserByIdQuery;
import the.garage.platform.iam.application.queries.GetUserByUsernameQuery;
import the.garage.platform.iam.application.queryservices.UserQueryService;
import the.garage.platform.iam.domain.model.valueobjects.RoleType;
import the.garage.platform.iam.interfaces.acl.IdentityAccessContextFacade;

@Service
public class IdentityAccessContextFacadeImpl implements IdentityAccessContextFacade {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public IdentityAccessContextFacadeImpl(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @Override
    public boolean suspendUser(Long userId) {
        var result = userCommandService.handle(new SuspendUserCommand(userId));
        return result.isSuccess();
    }

    @Override
    public boolean reactivateUser(Long userId) {
        var result = userCommandService.handle(new ReactivateUserCommand(userId));
        return result.isSuccess();
    }

    @Override
    public boolean changeUserRole(Long userId, String roleName) {
        try {
            var role = RoleType.valueOf(roleName);
            var result = userCommandService.handle(new ChangeUserRoleCommand(userId, role));
            return result.isSuccess();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Long fetchUserIdByUsername(String username) {
        return userQueryService.handle(new GetUserByUsernameQuery(username))
                .map(user -> user.getId())
                .orElse(0L);
    }

    @Override
    public String fetchUsernameByUserId(Long userId) {
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(user -> user.getUsername())
                .orElse("");
    }
}
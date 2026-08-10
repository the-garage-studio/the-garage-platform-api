package the.garage.platform.iam.application.interfaces.rest.transform;

import the.garage.platform.iam.application.commands.ChangeUserRoleCommand;
import the.garage.platform.iam.application.interfaces.rest.resources.ChangeUserRoleResource;
import the.garage.platform.iam.domain.model.valueobjects.RoleType;

public class ChangeUserRoleCommandFromResourceAssembler {
    public static ChangeUserRoleCommand toCommandFromResource(Long userId, ChangeUserRoleResource resource) {
        return new ChangeUserRoleCommand(userId, RoleType.valueOf(resource.role()));
    }
}
package the.garage.platform.iam.application.interfaces.rest.transform;

import the.garage.platform.iam.application.commands.SignInCommand;
import the.garage.platform.iam.application.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}
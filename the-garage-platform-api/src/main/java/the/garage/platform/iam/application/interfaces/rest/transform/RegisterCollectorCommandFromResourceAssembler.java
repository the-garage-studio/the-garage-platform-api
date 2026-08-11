package the.garage.platform.iam.application.interfaces.rest.transform;

import the.garage.platform.iam.application.commands.RegisterCollectorCommand;
import the.garage.platform.iam.application.interfaces.rest.resources.RegisterCollectorResource;

public class RegisterCollectorCommandFromResourceAssembler {
    public static RegisterCollectorCommand toCommandFromResource(RegisterCollectorResource resource) {
        return new RegisterCollectorCommand(
                resource.firstName(), resource.lastName(), resource.username(),
                resource.email(), resource.password());
    }
}
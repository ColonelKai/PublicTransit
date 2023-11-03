package org.colonelkai.publictransit.command.register;

import org.colonelkai.publictransit.commands.PublicTransitCommandLauncher;
import org.core.command.ArgumentLauncher;
import org.core.command.argument.ArgumentCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class RegisterCommandTests {

    @Test
    public void doesCommandGetRegisteredForPublicTransit() {
        ArgumentLauncher launcher = new PublicTransitCommandLauncher(PublicTransitCommandLauncher.PUBLIC_TRANSIT);

        //act
        Set<ArgumentCommand> commands = launcher.getCommands();

        //assert
        Assertions.assertFalse(commands.isEmpty());
    }

}

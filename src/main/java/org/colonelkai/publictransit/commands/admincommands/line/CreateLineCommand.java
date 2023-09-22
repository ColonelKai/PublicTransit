package org.colonelkai.publictransit.commands.admincommands.line;

import com.sun.jdi.connect.Connector;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.simple.StringArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CreateLineCommand implements ArgumentCommand {
    private final ExactArgument CREATE_ARGUMENT = new ExactArgument("CreateLine");
    private final StringArgument LINE_NAME_ARGUMENT = new StringArgument("linename");


    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(
                this.CREATE_ARGUMENT,
                this.LINE_NAME_ARGUMENT
        );
    }

    @Override
    public String getDescription() {
        return "Creates a new line.";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.empty(); // TODO Ask mose how to do this.
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        return false; // TODO woo, time to do the god class.
    }
}

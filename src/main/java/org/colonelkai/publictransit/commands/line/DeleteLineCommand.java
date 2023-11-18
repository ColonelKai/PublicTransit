package org.colonelkai.publictransit.commands.line;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DeleteLineCommand implements ArgumentCommand {

    private final ExactArgument DELETE_ARGUMENT = new ExactArgument("delete");
    private final ExactArgument LINE_ARGUMENT = new ExactArgument("line");
    private final LineArgument LINE_NAME_ARGUMENT = new LineArgument("linename");

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(LINE_ARGUMENT, DELETE_ARGUMENT, LINE_NAME_ARGUMENT);
    }

    @Override
    public String getDescription() {
        return "Deletes a created line";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.DELETE_LINE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, LINE_NAME_ARGUMENT);
        //cancel lines
        PublicTransit.getPlugin().getNodeManager().unregister(line);
        commandContext.getSource().sendMessage(Component.text("Deleted ").append(line.getName()));
        return true;
    }
}

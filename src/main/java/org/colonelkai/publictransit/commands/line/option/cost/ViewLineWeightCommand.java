package org.colonelkai.publictransit.commands.line.option.cost;

import org.colonelkai.publictransit.commands.line.option.ViewLineOptionCommand;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import java.util.Optional;

public class ViewLineWeightCommand extends ViewLineOptionCommand {

    public ViewLineWeightCommand() {
        super(new ExactArgument("direction"), line -> line.filter(l -> l.getWeight().isPresent()));
    }

    @Override
    public String getDescription() {
        return "See the direction of the nodes in the line";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.WEIGHT_VIEW);
    }

    @Override
    protected void sendMessage(CommandSource source, Line line) {
        source.sendPlainMessage("Weight: " + line.getWeight().orElse(0), null);
    }
}

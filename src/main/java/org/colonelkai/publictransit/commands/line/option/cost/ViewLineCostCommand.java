package org.colonelkai.publictransit.commands.line.option.cost;

import org.colonelkai.publictransit.commands.line.option.ViewLineOptionCommand;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.TranslateCore;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import java.util.Optional;

public class ViewLineCostCommand extends ViewLineOptionCommand {

    public ViewLineCostCommand() {
        super(new ExactArgument("cost", false, "cost", "costtype"));
    }

    @Override
    public String getDescription() {
        return "See the cost of the line";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.COST_VIEW);
    }

    @Override
    protected void sendMessage(CommandSource source, Line line) {
        source.sendPlainMessage("Cost: " + TranslateCore.getCurrencyManager().getDefaultCurrency().asDisplay(line.getCost()), null);
        source.sendPlainMessage("CostType: " + line.getCostType().name().toLowerCase(), null);
    }
}

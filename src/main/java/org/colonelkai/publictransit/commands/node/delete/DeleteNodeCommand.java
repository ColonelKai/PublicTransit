package org.colonelkai.publictransit.commands.node.delete;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DeleteNodeCommand implements ArgumentCommand {

    private final LineArgument lineArgument;
    private final NodeArgument nodeArgument;

    public DeleteNodeCommand() {
        this.lineArgument = new LineArgument("line", lineStream -> lineStream.filter(Line::isActive));
        this.nodeArgument = new NodeArgument("nodeName", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(DeleteNodeCommand.this, DeleteNodeCommand.this.lineArgument)
                .getNodes());

    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("node"), new ExactArgument("delete"), this.lineArgument, this.nodeArgument);
    }

    @Override
    public String getDescription() {
        return "Delete a node from a line";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.DELETE_NODE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        Node node = commandContext.getArgument(this, this.nodeArgument);
        //update
        Line updated = line.toBuilder().removeNode(node).build();
        PublicTransit.getPlugin().getNodeManager().update(updated);
        return true;
    }

    @Override
    public boolean hasPermission(CommandSource source) {
        if (PublicTransit.getPlugin().getNodeManager().getNodes().noneMatch(node -> true)) {
            return false;
        }
        return ArgumentCommand.super.hasPermission(source);
    }
}

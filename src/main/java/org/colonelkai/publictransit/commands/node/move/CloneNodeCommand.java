package org.colonelkai.publictransit.commands.node.move;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.arguments.simple.number.IntegerArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CloneNodeCommand implements ArgumentCommand {

    private final LineArgument lineArgument;
    private final OptionalArgument<Line> otherLineArgument;
    private final NodeArgument nodeArgument;
    private final IntegerArgument movedToArgument;

    public CloneNodeCommand() {
        this.lineArgument = new LineArgument("line", lineStream -> lineStream.filter(Line::isActive));
        this.otherLineArgument = new OptionalArgument<>(new LineArgument("toLine", lineStream -> lineStream.filter(Line::isActive)),
                                                        new ParseCommandArgument<>() {
                                                            @Override
                                                            public CommandArgumentResult<Line> parse(CommandContext context,
                                                                                                     CommandArgumentContext<Line> argument) throws IOException {
                                                                return CommandArgumentResult.from(argument,
                                                                                                  context.getArgument(CloneNodeCommand.this, lineArgument));
                                                            }
                                                        });
        this.nodeArgument = new NodeArgument("nodeName", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(CloneNodeCommand.this, CloneNodeCommand.this.lineArgument)
                .getNodes());
        this.movedToArgument = new IntegerArgument("newPosition");

    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("node"), new ExactArgument("move"), this.lineArgument, this.nodeArgument);
    }

    @Override
    public String getDescription() {
        return "Moves a node from a line to a new position";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.DELETE_NODE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Node node = commandContext.getArgument(this, this.nodeArgument);

        Line moveTo = commandContext.getArgument(this, this.otherLineArgument);
        int moveToPosition = commandContext.getArgument(this, this.movedToArgument);
        //update
        PublicTransit.getPlugin().getNodeManager().update(moveTo.toBuilder().addNodeAt(moveToPosition, node.toBuilder()).build());
        return true;
    }
}

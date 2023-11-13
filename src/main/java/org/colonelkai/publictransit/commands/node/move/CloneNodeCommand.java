package org.colonelkai.publictransit.commands.node.move;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.SuggestionArgument;
import org.core.command.argument.arguments.simple.number.IntegerArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CloneNodeCommand implements ArgumentCommand {

    private final LineArgument lineArgument;
    private final LineArgument otherLineArgument;
    private final NodeArgument nodeArgument;
    private final SuggestionArgument<Integer> movedToArgument;

    public CloneNodeCommand() {
        this.lineArgument = new LineArgument("line");
        this.otherLineArgument = new LineArgument("toLine");
        this.nodeArgument = new NodeArgument("nodeName", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(CloneNodeCommand.this, CloneNodeCommand.this.lineArgument)
                .getNodes());
        this.movedToArgument = new SuggestionArgument<>(new IntegerArgument("newPosition")) {
            @Override
            public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Integer> argument) throws NotEnoughArguments {
                Line line = commandContext.getArgument(CloneNodeCommand.this, otherLineArgument);
                Line originalLine = commandContext.getArgument(CloneNodeCommand.this, lineArgument);
                Integer originalPos = null;
                if (line.equals(originalLine)) {
                    Node node = commandContext.getArgument(CloneNodeCommand.this, nodeArgument);
                    originalPos = originalLine.getNodes().indexOf(node);
                }
                Integer finalOriginalPos = originalPos;
                int max = line.getNodes().size();
                String peek = commandContext.getCommand()[argument.getFirstArgument()];
                return IntStream
                        .range(0, max + 1)
                        .filter(v -> finalOriginalPos != null && finalOriginalPos != v)
                        .boxed()
                        .map(Object::toString)
                        .filter(v -> v.startsWith(peek))
                        .sorted()
                        .collect(Collectors.toList());
            }
        };

    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("node"), new ExactArgument("copy"), this.lineArgument, this.nodeArgument, otherLineArgument, movedToArgument);
    }

    @Override
    public String getDescription() {
        return "Moves a node from a line to a new position";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.COPY_NODE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line originalLine = commandContext.getArgument(this, this.lineArgument);
        Node node = commandContext.getArgument(this, this.nodeArgument);
        int originalPosition = originalLine.getNodes().indexOf(node);

        Line moveTo = commandContext.getArgument(this, this.otherLineArgument);
        if (originalLine.equals(moveTo)) {
            commandContext.getSource().sendMessage(Component.text("Cannot copy a node into the same line"));
            return false;
        }
        int moveToPosition = commandContext.getArgument(this, this.movedToArgument);
        //update
        PublicTransit.getPlugin().getNodeManager().update(moveTo.toBuilder().addNodeAt(moveToPosition, node.toBuilder()).build());
        commandContext
                .getSource()
                .sendMessage(Component
                                     .text("Copied '")
                                     .append(originalLine.getName())
                                     .append(Component.text("':'" + originalPosition + "' to '"))
                                     .append(moveTo.getName())
                                     .append(Component.text("':'" + moveToPosition + "'")));
        return true;
    }
}

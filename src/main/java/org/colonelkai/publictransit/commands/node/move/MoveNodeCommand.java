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
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.arguments.operation.SuggestionArgument;
import org.core.command.argument.arguments.simple.number.IntegerArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MoveNodeCommand implements ArgumentCommand {

    private final LineArgument lineArgument;
    private final OptionalArgument<Line> otherLineArgument;
    private final NodeArgument nodeArgument;
    private final SuggestionArgument<Integer> movedToArgument;

    public MoveNodeCommand() {
        this.lineArgument = new LineArgument("line");
        this.otherLineArgument = new OptionalArgument<>(new LineArgument("toLine"), new ParseCommandArgument<>() {
            @Override
            public CommandArgumentResult<Line> parse(CommandContext context, CommandArgumentContext<Line> argument) throws IOException {
                return CommandArgumentResult.from(argument, context.getArgument(MoveNodeCommand.this, lineArgument));
            }
        });
        this.nodeArgument = new NodeArgument("nodeName", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(MoveNodeCommand.this, MoveNodeCommand.this.lineArgument)
                .getNodes());
        this.movedToArgument = new SuggestionArgument<>(new IntegerArgument("newPosition")) {
            @Override
            public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Integer> argument) throws NotEnoughArguments {
                Line line = commandContext.getArgument(MoveNodeCommand.this, otherLineArgument);
                Line originalLine = commandContext.getArgument(MoveNodeCommand.this, lineArgument);
                Integer originalPos = null;
                if (line.equals(originalLine)) {
                    Node node = commandContext.getArgument(MoveNodeCommand.this, nodeArgument);
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
        return Arrays.asList(new ExactArgument("node"), new ExactArgument("move"), this.lineArgument, this.nodeArgument, this.otherLineArgument,
                             this.movedToArgument);
    }

    @Override
    public String getDescription() {
        return "Moves a node from a line to a new position";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.MOVE_NODE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        Node node = commandContext.getArgument(this, this.nodeArgument);

        Line moveTo = commandContext.getArgument(this, this.otherLineArgument);
        int moveToPosition = commandContext.getArgument(this, this.movedToArgument);
        boolean isLineSame = line.equals(moveTo);
        int originalPosition = line.getNodes().indexOf(node);

        //update
        line = line.toBuilder().removeNode(node).build();
        if (isLineSame) {
            moveTo = line;
        }
        moveTo = moveTo.toBuilder().addNodeAt(moveToPosition, node.toBuilder()).build();
        PublicTransit.getPlugin().getNodeManager().update(line);
        PublicTransit.getPlugin().getNodeManager().update(moveTo);
        commandContext
                .getSource()
                .sendMessage(Component
                                     .text("Moved '")
                                     .append(line.getName())
                                     .append(Component.text("':'" + originalPosition + "' to '"))
                                     .append(moveTo.getName())
                                     .append(Component.text("':'" + moveToPosition + "'")));
        return true;
    }
}

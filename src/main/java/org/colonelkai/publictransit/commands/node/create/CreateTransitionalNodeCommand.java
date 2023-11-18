package org.colonelkai.publictransit.commands.node.create;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.arguments.operation.SuggestionArgument;
import org.core.command.argument.arguments.position.ExactPositionArgument;
import org.core.command.argument.arguments.simple.number.IntegerArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.world.position.Positionable;
import org.core.world.position.impl.ExactPosition;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateTransitionalNodeCommand implements ArgumentCommand {

    private LineArgument lineArgument = new LineArgument("line");
    private OptionalArgument<ExactPosition> locationArgument = new OptionalArgument<>(new ExactPositionArgument("location"), new ParseCommandArgument<>() {
        @Override
        public CommandArgumentResult<ExactPosition> parse(CommandContext context, CommandArgumentContext<ExactPosition> argument) throws IOException {
            if (!(context.getSource() instanceof Positionable<?>)) {
                throw new IOException("location must be specified");
            }

            ExactPosition position = ((Positionable<?>) context.getSource()).getPosition().toExactPosition();
            return CommandArgumentResult.from(argument, 0, position);
        }
    });
    private SuggestionArgument<Integer> positionArgument = new SuggestionArgument<>(new IntegerArgument("position")) {
        @Override
        public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Integer> argument) throws NotEnoughArguments {
            int max = commandContext.getArgument(CreateTransitionalNodeCommand.this, lineArgument).getNodes().size();
            String command = commandContext.getCommand()[argument.getFirstArgument()];
            return IntStream.range(0, max + 1).boxed().map(Object::toString).filter(v -> v.startsWith(command)).collect(Collectors.toList());
        }
    };

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("node"), new ExactArgument("create"), new ExactArgument("transitional"), lineArgument, positionArgument,
                             locationArgument);
    }

    @Override
    public String getDescription() {
        return "Creates a node that players travel through";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.CREATE_NODE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        int nodePosition = commandContext.getArgument(this, this.positionArgument);
        ExactPosition position = commandContext.getArgument(this, this.locationArgument);

        if ((line.getNodes().size() < nodePosition) || (0 > nodePosition)) {
            commandContext
                    .getSource()
                    .sendMessage(Component
                                         .text("Position must be between 0 and " + line.getNodes().size() + " for ")
                                         .color(NamedTextColor.RED)
                                         .append(line.getName()));
            return false;
        }

        Optional<Node> optionalOverlappingNode = PublicTransit
                .getPlugin()
                .getNodeManager()
                .getNodes()
                .filter(compareNode -> NodeType.TRANSITIONAL == compareNode.getNodeType())
                .filter(compareNode -> compareNode.isWithin(position))
                .findAny();
        if (optionalOverlappingNode.isPresent()) {
            Line foundLine = PublicTransit
                    .getPlugin()
                    .getNodeManager()
                    .getLinesFor(optionalOverlappingNode.get())
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("Broken logic"));
            int index = foundLine.getNodes().indexOf(optionalOverlappingNode.get());

            commandContext
                    .getSource()
                    .sendMessage(Component.text("The node of '" + index + "' in ").append(foundLine.getName()).append(Component.text("is at this location")));
            return false;
        }
        //cancel line
        NodeBuilder node = new NodeBuilder().setType(NodeType.TRANSITIONAL).setPosition(position);
        line = line.toBuilder().addNodeAt(nodePosition, node).build();
        PublicTransit.getPlugin().getNodeManager().update(line);
        try {
            line.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        commandContext.getSource().sendMessage(Component.text("Created stop node of " + node.name()));
        return true;
    }
}

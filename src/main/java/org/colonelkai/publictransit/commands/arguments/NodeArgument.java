package org.colonelkai.publictransit.commands.arguments;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.lamda.ThrowableBiFunction;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.source.command.CommandSource;
import org.core.world.position.Positionable;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeArgument implements CommandArgument<Node> {

    private final String id;
    private final ThrowableBiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>, NotEnoughArguments> function;
    private final Function<Stream<Node>, Stream<Node>> filter;

    public NodeArgument(String id, ThrowableBiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>, NotEnoughArguments> function) {
        this(id, function, s -> s);
    }

    public NodeArgument(String id,
                        ThrowableBiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>, NotEnoughArguments> function,
                        Function<Stream<Node>, Stream<Node>> filter) {
        this.id = id;
        this.function = function;
        this.filter = filter;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<Node> parse(CommandContext commandContext, CommandArgumentContext<Node> argument) throws IOException {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        List<Node> list = this.function.apply(commandContext, argument);
        Node node;
        try {
            int index = Integer.parseInt(peek);
            if (list.size() <= index) {
                throw new IOException("No node at that number");
            }
            node = list.get(index);
        } catch (NumberFormatException e) {
            node = list
                    .stream()
                    .filter(n -> n.getName().isPresent())
                    .filter(n -> n.getName().get().equalsIgnoreCase(peek))
                    .findAny()
                    .orElseThrow(() -> new IOException("no node by that name"));
        }
        node = this.filter.apply(Stream.of(node)).findAny().orElseThrow(() -> new IOException("Invalid node"));
        return CommandArgumentResult.from(argument, node);
    }

    @Override
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Node> argument) throws NotEnoughArguments {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        List<Node> list = this.function.apply(commandContext, argument);
        Stream<Node> target = this.filter.apply(list.stream());
        Function<Stream<Node>, List<String>> toDisplay = (stream) -> stream.flatMap(node -> {
            Optional<String> opName = node.getName();
            String display = list.indexOf(node) + "";
            if (opName.isPresent()) {
                return Stream.of(display, opName.get());
            }
            return Stream.of(display);
        }).sorted().collect(Collectors.toList());


        if (peek.isEmpty()) {
            return toDisplay.apply(target);
        }
        return toDisplay.apply(target).stream().filter(name -> name.toLowerCase().startsWith(peek.toLowerCase())).sorted().collect(Collectors.toList());
    }

    public static OptionalArgument<Node> nodeOrClosest(String id,
                                                       ThrowableBiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>, NotEnoughArguments> function,
                                                       Function<Stream<Node>, Stream<Node>> filter) {
        return nodeOrClosest(new NodeArgument(id, function, filter));
    }

    private static OptionalArgument<Node> nodeOrClosest(NodeArgument nodeArgument) {
        ParseCommandArgument<Node> elser = (context, argument) -> {
            CommandSource source = context.getSource();
            if (!(source instanceof Positionable<?> positionable)) {
                throw new IOException("Line must be specified");
            }
            NodeManager manager = PublicTransit.getPlugin().getNodeManager();
            Optional<Node> opNode = manager.getClosestNode(positionable);
            if (opNode.isEmpty()) {
                throw new IOException("No nodes near you");
            }
            return CommandArgumentResult.from(argument, argument.getFirstArgument(), opNode.get());
        };
        return new OptionalArgument<>(nodeArgument, elser);
    }
}

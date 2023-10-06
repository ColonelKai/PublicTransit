package org.colonelkai.publictransit.commands.arguments;

import org.colonelkai.publictransit.node.Node;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeArgument implements CommandArgument<Node> {

    private final String id;
    private final BiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>> function;
    private final Function<Stream<Node>, Stream<Node>> filter;

    public NodeArgument(String id, BiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>> function) {
        this(id, function, s -> s);
    }

    public NodeArgument(String id, BiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>> function, Function<Stream<Node>, Stream<Node>> filter) {
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
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Node> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        List<Node> list = this.function.apply(commandContext, argument);
        Stream<Node> target = this.filter.apply(list.stream());
        Function<Stream<Node>, List<String>> toDisplay = (stream) -> stream.flatMap(node -> {
            var opName = node.getName();
            var display = list.indexOf(node) + "";
            if (opName.isPresent()) {
                return Stream.of(display, opName.get());
            }
            return Stream.of(display);
        }).sorted().collect(Collectors.toList());


        if (peek.isEmpty()) {
            return toDisplay.apply(target);
        }
        return toDisplay.apply(target).stream().filter(name -> name.toLowerCase().startsWith(peek.toLowerCase())).collect(Collectors.toList());
    }
}

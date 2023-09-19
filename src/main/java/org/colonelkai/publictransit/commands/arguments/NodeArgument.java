package org.colonelkai.publictransit.commands.arguments;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.node.Node;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeArgument implements CommandArgument<Node> {

    private final String id;
    private final Function<Stream<Node>, Stream<Node>> filters;

    public NodeArgument(@NotNull String id) {
        this(id, s -> s);
    }

    public NodeArgument(@NotNull String id, Function<Stream<Node>, Stream<Node>> filters) {
        this.id = id;
        this.filters = filters;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<Node> parse(CommandContext context, CommandArgumentContext<Node> argument) throws IOException {
        var arg = context.getCommand()[argument.getFirstArgument()];
        var nodeManager = PublicTransit.getPlugin().getNodeManager();
        var stream = nodeManager.getNodes().filter(node -> node.getName().equalsIgnoreCase(arg));
        var result = this.filters.apply(stream).findAny().orElseThrow(() -> new IOException("Unknown node"));
        return CommandArgumentResult.from(argument, result);
    }

    @Override
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Node> argument) {
        var peek = commandContext.getCommand()[argument.getFirstArgument()];
        var nodeManager = PublicTransit.getPlugin().getNodeManager();
        var stream = nodeManager.getNodes().filter(node -> node.getName().toLowerCase().startsWith(peek.toLowerCase()));
        stream = this.filters.apply(stream);
        return stream.map(Node::getName).sorted().collect(Collectors.toList());
    }
}

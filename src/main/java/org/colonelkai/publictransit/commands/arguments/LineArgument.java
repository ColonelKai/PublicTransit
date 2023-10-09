package org.colonelkai.publictransit.commands.arguments;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.MappedArgumentWrapper;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.context.CommandArgumentContext;
import org.core.command.argument.context.CommandContext;
import org.core.source.command.CommandSource;
import org.core.world.position.Positionable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineArgument implements CommandArgument<Line> {

    private final String id;
    private final Function<Stream<Line>, Stream<Line>> filters;

    public LineArgument(@NotNull String id) {
        this(id, s -> s);
    }

    public LineArgument(@NotNull String id, Function<Stream<Line>, Stream<Line>> filters) {
        this.id = id;
        this.filters = filters;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public CommandArgumentResult<Line> parse(CommandContext context, CommandArgumentContext<Line> argument) throws IOException {
        String arg = context.getCommand()[argument.getFirstArgument()];
        NodeManager nodeManager = PublicTransit.getPlugin().getNodeManager();
        Stream<Line> stream = nodeManager.getLines().stream().filter(line -> line.getIdentifier().equalsIgnoreCase(arg));
        Line result = this.filters.apply(stream).findAny().orElseThrow(() -> new IOException("Unknown node"));
        return CommandArgumentResult.from(argument, result);
    }

    @Override
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Line> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        NodeManager nodeManager = PublicTransit.getPlugin().getNodeManager();
        Stream<Line> stream = nodeManager.getLines().stream().filter(line -> line.getIdentifier().toLowerCase().startsWith(peek.toLowerCase()));
        stream = this.filters.apply(stream);
        return stream.map(Line::getIdentifier).sorted().collect(Collectors.toList());
    }

    public static OptionalArgument<List<Line>> linesOrClosest(String id) {
        return linesOrClosest(new LineArgument(id));
    }

    public static OptionalArgument<List<Line>> linesOrClosest(String id, Function<Stream<Line>, Stream<Line>> filter) {
        return linesOrClosest(new LineArgument(id, filter));
    }

    private static OptionalArgument<List<Line>> linesOrClosest(LineArgument filter) {
        MappedArgumentWrapper<List<Line>, Line> lineArgument = new MappedArgumentWrapper<>(filter, Collections::singletonList);
        ParseCommandArgument<List<Line>> elser = (context, argument) -> {
            CommandSource source = context.getSource();
            if (!(source instanceof Positionable<?> positionable)) {
                throw new IOException("Line must be specified");
            }
            NodeManager manager = PublicTransit.getPlugin().getNodeManager();
            Optional<Node> opNode = manager.getClosestNode(positionable);
            if (opNode.isEmpty()) {
                throw new IOException("No nodes near you");
            }
            List<Line> lines = manager.getLinesFor(opNode.get()).collect(Collectors.toList());
            return CommandArgumentResult.from(argument, argument.getFirstArgument(), lines);
        };
        return new OptionalArgument<>(lineArgument, elser);
    }
}

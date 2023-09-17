package org.colonelkai.publictransit.commands.arguments;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.line.Line;
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
        var arg = context.getCommand()[argument.getFirstArgument()];
        var nodeManager = PublicTransit.getPlugin().getNodeManager();
        var stream = nodeManager.getLines().stream().filter(line -> line.getIdentifier().equalsIgnoreCase(arg));
        var result = this.filters.apply(stream).findAny().orElseThrow(() -> new IOException("Unknown node"));
        return CommandArgumentResult.from(argument, result);
    }

    @Override
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Line> argument) {
        var peek = commandContext.getCommand()[argument.getFirstArgument()];
        var nodeManager = PublicTransit.getPlugin().getNodeManager();
        var stream = nodeManager.getLines().stream().filter(line -> line.getIdentifier().toLowerCase().startsWith(peek.toLowerCase()));
        stream = this.filters.apply(stream);
        return stream.map(Line::getIdentifier).sorted().collect(Collectors.toList());
    }
}

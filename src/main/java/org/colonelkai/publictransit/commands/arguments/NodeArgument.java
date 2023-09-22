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
import java.util.stream.Collectors;

public class NodeArgument implements CommandArgument<Node> {

    private final String id;
    private final BiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>> function;

    public NodeArgument(String id, BiFunction<CommandContext, CommandArgumentContext<Node>, List<Node>> function) {
        this.id = id;
        this.function = function;
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
        return CommandArgumentResult.from(argument, node);
    }

    @Override
    public Collection<String> suggest(CommandContext commandContext, CommandArgumentContext<Node> argument) {
        String peek = commandContext.getCommand()[argument.getFirstArgument()];
        List<Node> list = this.function.apply(commandContext, argument);
        try {
            Integer.parseInt(peek);
            return list.stream().map(n -> list.indexOf(n) + "").filter(t -> t.startsWith(peek)).collect(Collectors.toList());
        } catch (NumberFormatException e) {
            return list
                    .stream()
                    .filter(n -> n.getName().isPresent())
                    .map(n -> n.getName().get())
                    .filter(name -> name.toLowerCase().startsWith(peek.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }
}

package org.colonelkai.publictransit.commands.node.option;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.options.CommandOptionBuilder;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SetNodeOptionCommand implements ArgumentCommand {

    private final ExactArgument NODE_ARGUMENT = new ExactArgument("node");
    private final ExactArgument VIEW_ARGUMENT = new ExactArgument("set");
    private final ExactArgument nameArgument;
    private final LineArgument lineArgument;
    private final NodeArgument nodeArgument;
    private final CommandArgument<?> valueArgument;
    private final Method setter;


    public SetNodeOptionCommand(ExactArgument argument, CommandArgument<?> valueArgument, Method setter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename");
        this.valueArgument = valueArgument;
        this.nodeArgument = new NodeArgument("nodevalue", (commandContext, nodeCommandArgumentContext) -> {
            Line line = commandContext.getArgument(SetNodeOptionCommand.this, SetNodeOptionCommand.this.lineArgument);
            return line.getNodes();
        });
        this.setter = setter;
    }

    public SetNodeOptionCommand(ExactArgument argument, CommandArgument<?> valueArgument, Method setter, Function<Stream<Line>, Stream<Line>> filter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename", filter);
        this.valueArgument = valueArgument;
        this.nodeArgument = new NodeArgument("node", (commandContext, nodeCommandArgumentContext) -> {
            Line line = commandContext.getArgument(SetNodeOptionCommand.this, SetNodeOptionCommand.this.lineArgument);
            return line.getNodes();
        });
        this.setter = setter;
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(this.NODE_ARGUMENT, this.VIEW_ARGUMENT, this.lineArgument, this.nodeArgument, this.nameArgument, this.valueArgument);
    }

    @Override
    public String getDescription() {
        return "set the " + this.nameArgument.getId() + " of a node";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.SET_NODE_OPTION);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        return runCommand(commandContext, args);
    }

    private <T> boolean runCommand(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        Node node = commandContext.getArgument(this, this.nodeArgument);
        LineBuilder builder = line.toBuilder().removeNode(node);
        T value = (T) commandContext.getArgument(this, this.valueArgument);
        NodeBuilder nodeBuilder;
        try {
            nodeBuilder = ((NodeBuilder) this.setter.invoke(node.toBuilder(), value));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        PublicTransit.getPlugin().getNodeManager().update(builder.addNodes(nodeBuilder).build());
        try {
            line.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        commandContext.getSource().sendMessage(Component.text("updated line"));
        return true;
    }

    public static Collection<SetNodeOptionCommand> createSetCommands() {
        return CommandOptionBuilder
                .buildFrom(NodeBuilder.class)
                .stream()
                .map(meta -> new SetNodeOptionCommand(meta.nameArgument(), meta.setterArgument(), meta.setter()))
                .collect(Collectors.toList());
    }
}

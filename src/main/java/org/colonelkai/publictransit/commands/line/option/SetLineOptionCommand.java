package org.colonelkai.publictransit.commands.line.option;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
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

public class SetLineOptionCommand implements ArgumentCommand {

    private final ExactArgument LINE_ARGUMENT = new ExactArgument("line");
    private final ExactArgument VIEW_ARGUMENT = new ExactArgument("view");
    private final ExactArgument nameArgument;
    private final LineArgument lineArgument;
    private final CommandArgument<?> valueArgument;
    private final Method setter;


    public SetLineOptionCommand(ExactArgument argument, CommandArgument<?> valueArgument, Method setter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename");
        this.valueArgument = valueArgument;
        this.setter = setter;
    }

    public SetLineOptionCommand(ExactArgument argument, CommandArgument<?> valueArgument, Method setter, Function<Stream<Line>, Stream<Line>> filter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename", filter);
        this.valueArgument = valueArgument;
        this.setter = setter;
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(this.LINE_ARGUMENT, this.VIEW_ARGUMENT, this.lineArgument, this.nameArgument);
    }

    @Override
    public String getDescription() {
        return "set the " + this.nameArgument.getId() + " of a node";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.SET_LINE_OPTION);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        return runCommand(commandContext, args);
    }

    private <T> boolean runCommand(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        LineBuilder builder = line.toBuilder();
        T value = (T) commandContext.getArgument(this, this.valueArgument);
        NodeBuilder nodeBuilder;
        try {
            builder = ((LineBuilder) this.setter.invoke(builder, value));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        Line updated = builder.build();

        PublicTransit.getPlugin().getNodeManager().update(updated);
        try {
            updated.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        commandContext.getSource().sendMessage(Component.text("updated line"));
        return true;
    }

    public static Collection<SetLineOptionCommand> createSetCommands() {
        return CommandOptionBuilder
                .buildFrom(LineBuilder.class)
                .stream()
                .map(meta -> new SetLineOptionCommand(meta.nameArgument(), meta.setterArgument(), meta.setter()))
                .collect(Collectors.toList());
    }
}

package org.colonelkai.publictransit.commands.node.option;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
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

public class ViewNodeOptionCommand implements ArgumentCommand {

    private final ExactArgument LINE_ARGUMENT = new ExactArgument("line");
    private final ExactArgument VIEW_ARGUMENT = new ExactArgument("view");
    private final Method getter;
    private final ExactArgument nameArgument;
    private final LineArgument lineArgument;


    public ViewNodeOptionCommand(ExactArgument argument, Method getter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename");
        this.getter = getter;
    }

    public ViewNodeOptionCommand(ExactArgument argument, Method method, Function<Stream<Line>, Stream<Line>> filter) {
        this.nameArgument = argument;
        this.lineArgument = new LineArgument("linename", filter);
        this.getter = method;
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(this.LINE_ARGUMENT, this.VIEW_ARGUMENT, this.lineArgument, this.nameArgument);
    }

    @Override
    public String getDescription() {
        return "view the " + this.nameArgument.getId() + " of a line";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.VIEW_NODE_OPTION);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        Object value;
        try {
            value = this.getter.invoke(line.toBuilder());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        String stringValue = toString(value).orElse(value.toString());
        commandContext.getSource().sendMessage(Component.text(this.nameArgument.getId() + ":" + stringValue));
        return true;
    }

    public static Collection<ViewNodeOptionCommand> createViewCommands() {
        return CommandOptionBuilder
                .buildFrom(LineBuilder.class)
                .stream()
                .map(meta -> new ViewNodeOptionCommand(meta.nameArgument(), meta.getter()))
                .collect(Collectors.toList());
    }

    public static Optional<String> toString(Object obj) {
        if (obj == null) {
            return Optional.of("none");
        }
        if (obj instanceof Optional<?> optional) {
            return Optional.of(optional.flatMap(ViewNodeOptionCommand::toString).orElse("none"));
        }
        return Optional.empty();
    }
}

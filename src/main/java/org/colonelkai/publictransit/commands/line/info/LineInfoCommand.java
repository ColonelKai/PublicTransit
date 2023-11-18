package org.colonelkai.publictransit.commands.line.info;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.node.option.ViewNodeOptionCommand;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.options.CommandOptionBuilder;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.context.CommandContext;
import org.core.entity.living.human.player.Player;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class LineInfoCommand implements ArgumentCommand {

    private LineArgument lineArgument;

    public LineInfoCommand() {
        this.lineArgument = new LineArgument("line");
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("info"), lineArgument);
    }

    @Override
    public String getDescription() {
        return "Get info on a node";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.VIEW_NODE_OPTION);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Line line = commandContext.getArgument(this, this.lineArgument);
        commandContext.getSource().sendMessage(Component.text("==[").append(line.getName()).append(Component.text("]==")));

        //manual
        commandContext.getSource().sendMessage(Component.text("Nodes: " + line.getNodes().size()));
        commandContext.getSource().sendMessage(Component.text("Is Active: " + line.isActive()));
        if (commandContext.getSource() instanceof Player<?> player) {
            commandContext.getSource().sendMessage(Component.text("Within weight: " + line.isValidWeight(player)));
        }

        //automatic
        LineBuilder builder = line.toBuilder();
        CommandOptionBuilder.buildFrom(builder.getClass()).forEach(meta -> {
            try {
                Object result = meta.getter().invoke(builder);
                Component asDisplay = ViewNodeOptionCommand.toString(result).orElseGet(() -> Component.text(result.toString()));
                commandContext.getSource().sendMessage(Component.text(meta.nameArgument().getId() + ": ").append(asDisplay));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });

        return true;
    }
}

package org.colonelkai.publictransit.commands.node.info;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.commands.node.option.ViewNodeOptionCommand;
import org.colonelkai.publictransit.line.Line;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class NodeInfoCommand implements ArgumentCommand {

    private final LineArgument lineArgument;
    private final NodeArgument nodeArgument;

    public NodeInfoCommand() {
        this.lineArgument = new LineArgument("line");
        this.nodeArgument = new NodeArgument("node", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(NodeInfoCommand.this, this.lineArgument)
                .getNodes());
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(new ExactArgument("info"), this.lineArgument, this.nodeArgument);
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
        Node node = commandContext.getArgument(this, this.nodeArgument);
        String identifier = node.getName().orElseGet(() -> line.getNodes().indexOf(node) + "");
        commandContext.getSource().sendMessage(Component.text("==[").append(line.getName()).append(Component.text(":" + identifier + "]==")));

        //manual
        commandContext.getSource().sendMessage(Component.text("Position: " + line.getNodes().indexOf(node)));

        //automatic
        NodeBuilder builder = node.toBuilder();
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

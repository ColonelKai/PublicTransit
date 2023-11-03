package org.colonelkai.publictransit.commands.line;

import com.sun.jdi.connect.Connector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.TranslateCore;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.arguments.operation.RemainingArgument;
import org.core.command.argument.arguments.simple.StringArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.utils.ComponentUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CreateLineCommand implements ArgumentCommand {
    private final ExactArgument CREATE_ARGUMENT = new ExactArgument("create");
    private final ExactArgument LINE_ARGUMENT = new ExactArgument("line");
    private final StringArgument LINE_NAME_ARGUMENT = new StringArgument("linename");

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(this.LINE_ARGUMENT, this.CREATE_ARGUMENT, this.LINE_NAME_ARGUMENT);
    }

    @Override
    public String getDescription() {
        return "Creates a new line.";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.CREATE_LINE);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        Component lineDisplayName = ComponentUtils.fromLegacy(commandContext.getArgument(this, LINE_NAME_ARGUMENT));
        String lineName = ComponentUtils.toPlain(lineDisplayName);
        if (PublicTransit.getPlugin().getNodeManager().getLine(lineName).isPresent()) {
            commandContext.getSource().sendMessage(Component.text("Line already with the name of ").color(NamedTextColor.RED).append(lineDisplayName));
            return false;
        }


        Line line = new LineBuilder().setIdentifier(lineName).setName(lineDisplayName).setBiDirectional(true).build();
        PublicTransit.getPlugin().getNodeManager().register(line);
        try {
            line.save();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        commandContext.getSource().sendMessage(Component.text("Created ").append(lineDisplayName));
        return true;
    }
}

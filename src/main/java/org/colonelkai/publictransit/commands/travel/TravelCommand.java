package org.colonelkai.publictransit.commands.travel;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.travel.Travel;
import org.colonelkai.publictransit.line.travel.TravelBuilder;
import org.colonelkai.publictransit.line.travel.TravelSchedule;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.Permissions;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.context.CommandContext;
import org.core.entity.living.human.player.LivePlayer;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.schedule.Scheduler;
import org.core.source.command.CommandSource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TravelCommand implements ArgumentCommand {

    private OptionalArgument<Node> fromNode;
    private OptionalArgument<List<Line>> lineArgument;
    private NodeArgument toNode;

    public TravelCommand() {
        this.lineArgument = LineArgument.linesOrClosest("line", stream -> stream.filter(Line::isActive));
        this.toNode = new NodeArgument("to", ((commandContext, nodeCommandArgumentContext) -> {
            List<Line> lines = commandContext.getArgument(TravelCommand.this, this.lineArgument);
            return lines.stream().flatMap(line -> line.getNodes().stream()).toList();
        }), stream -> stream.filter(node -> NodeType.STOP == node.getNodeType()));
        this.fromNode = NodeArgument.nodeOrClosest("from", (commandContext, nodeCommandArgumentContext) -> {
            Node to = commandContext.getArgument(TravelCommand.this, this.toNode);
            return commandContext
                    .getArgument(TravelCommand.this, this.lineArgument)
                    .stream()
                    .filter(line -> line.getNodes().contains(to))
                    .flatMap(line -> line.getNodes().stream())
                    .toList();
        }, stream -> stream.filter(node -> NodeType.STOP == node.getNodeType()));
    }

    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(lineArgument, toNode, fromNode);
    }

    @Override
    public String getDescription() {
        return "Travel on a node";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.of(Permissions.TRAVEL);
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        if (!(commandContext.getSource() instanceof LivePlayer player)) {
            return false;
        }
        List<Line> lines = commandContext.getArgument(TravelCommand.this, this.lineArgument);
        Node from = commandContext.getArgument(TravelCommand.this, this.fromNode);
        Optional<Line> opLine = lines.stream().filter(line -> line.getNodes().contains(from)).findAny();
        if (opLine.isEmpty()) {
            //this should never be hit
            commandContext.getSource().sendMessage(Component.text("Cannot find specified node"));
            return false;
        }
        Line line = opLine.get();
        Node to = commandContext.getArgument(this, this.toNode);

        if (from.equals(to)) {
            commandContext
                    .getSource()
                    .sendMessage(Component.text("Both to and from are the same.").color(NamedTextColor.RED));
            return false;
        }
        if (!player.hasPermission(Permissions.IGNORE_DISTANCE_CHECK) && !from.isWithin(player)) {
            commandContext.getSource().sendMessage(Component.text("Not close enough to " + line.getIdentifier(from)));
            return false;
        }

        if (!line.isBiDirectional()) {
            int fromIndex = line.getNodes().indexOf(from);
            int toIndex = line.getNodes().indexOf(to);
            if (!line.getDirection().isValid(fromIndex, toIndex)) {
                commandContext.getSource().sendMessage(Component.text("Line is heading in the incorrect direction"));
                return false;
            }
        }

        if (!line.isValidWeight(player)) {
            commandContext.getSource().sendMessage(Component.text("You are too heavy. Loose some weight"));
            return false;
        }

        Travel travel = new TravelBuilder()
                .setTravellingOn(line)
                .setPlayer(player)
                .setEndingNode(to)
                .setCurrentNode(from)
                .build();
        Scheduler schedule = TravelSchedule.schedule(travel);
        player.sendMessage(
                Component.text("travelling from " + line.getIdentifier(from) + " - to " + line.getIdentifier(to)));
        schedule.run();
        return true;
    }

    @Override
    public boolean hasPermission(CommandSource source) {
        if (!(source instanceof LivePlayer)) {
            return false;
        }
        return ArgumentCommand.super.hasPermission(source);
    }
}

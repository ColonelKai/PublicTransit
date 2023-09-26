package org.colonelkai.publictransit.commands.cost;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.core.adventureText.AText;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.CommandArgumentResult;
import org.core.command.argument.ParseCommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.MappedArgumentWrapper;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.context.CommandContext;
import org.core.exceptions.NotEnoughArguments;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;
import org.core.source.viewer.CommandViewer;
import org.core.world.position.Positionable;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CostBetweenCommand implements ArgumentCommand {

    private final ExactArgument costArgument;
    private final NodeArgument firstArgument;
    private final NodeArgument lastArgument;
    private final OptionalArgument<List<Line>> lineArgument;

    public CostBetweenCommand() {
        this.costArgument = new ExactArgument("cost");
        this.lineArgument = LineArgument.linesOrClosest("line");
        this.firstArgument = new NodeArgument("first", (commandContext, nodeCommandArgumentContext) -> commandContext
                .getArgument(CostBetweenCommand.this, CostBetweenCommand.this.lineArgument)
                .stream()
                .flatMap(line -> line.getNodes().stream())
                .collect(Collectors.toList()));
        this.lastArgument = new NodeArgument("last", (commandContext, nodeCommandArgumentContext) -> {
            Node firstNode = commandContext.getArgument(CostBetweenCommand.this, CostBetweenCommand.this.firstArgument);
            return commandContext
                    .getArgument(CostBetweenCommand.this, CostBetweenCommand.this.lineArgument)
                    .stream()
                    .filter(line -> line.getNodes().contains(firstNode))
                    .flatMap(line -> line.getNodes().stream())
                    .collect(Collectors.toList());
        });
    }


    @Override
    public List<CommandArgument<?>> getArguments() {
        return Arrays.asList(this.costArgument, this.lineArgument, this.firstArgument, this.lastArgument);
    }

    @Override
    public String getDescription() {
        return "Gets the cost between nodes";
    }

    @Override
    public Optional<Permission> getPermissionNode() {
        return Optional.empty();
    }

    @Override
    public boolean run(CommandContext commandContext, String... args) throws NotEnoughArguments {
        if (commandContext.getSource() instanceof CommandViewer) {
            return false;
        }
        String currency = ""; //bukkit vault doesnt supports currencies ....
        CommandViewer source = (CommandViewer) commandContext.getSource();
        Node node = commandContext.getArgument(this, this.firstArgument);
        Node compare = commandContext.getArgument(this, this.lastArgument);
        if (node.equals(compare)) {
            //node going to itself
            source.sendMessage(this.createCostMessage(currency, 0));
            return true;
        }
        Line line = PublicTransit
                .getPlugin()
                .getNodeManager()
                .getLinesFor(node, compare)
                .min(Comparator.comparing(line2 -> line2.getPrice(node, compare)))
                .orElseThrow(() -> new RuntimeException("Broken logic"));

        double price = line.getPrice(node, compare);

        source.sendMessage(this.createCostMessage(currency, price));
        return true;
    }

    private AText createCostMessage(String currency, double price) {
        return AText.ofPlain("Your journey will cost " + currency + price);
    }
}

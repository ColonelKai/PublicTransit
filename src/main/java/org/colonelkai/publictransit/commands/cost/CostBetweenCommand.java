package org.colonelkai.publictransit.commands.cost;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.commands.arguments.LineArgument;
import org.colonelkai.publictransit.commands.arguments.NodeArgument;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeType;
import org.core.TranslateCore;
import org.core.command.argument.ArgumentCommand;
import org.core.command.argument.CommandArgument;
import org.core.command.argument.arguments.operation.ExactArgument;
import org.core.command.argument.arguments.operation.OptionalArgument;
import org.core.command.argument.context.CommandContext;
import org.core.eco.Currency;
import org.core.permission.Permission;
import org.core.source.command.CommandSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
                .collect(Collectors.toList()), stream -> stream.filter(node -> NodeType.STOP == node.getNodeType()));
        this.lastArgument = new NodeArgument("last", (commandContext, nodeCommandArgumentContext) -> {
            Node firstNode = commandContext.getArgument(CostBetweenCommand.this, CostBetweenCommand.this.firstArgument);
            return commandContext
                    .getArgument(CostBetweenCommand.this, CostBetweenCommand.this.lineArgument)
                    .stream()
                    .filter(line -> line.getNodes().contains(firstNode))
                    .flatMap(line -> line.getNodes().stream())
                    .collect(Collectors.toList());
        }, stream -> stream.filter(node -> NodeType.STOP == node.getNodeType()));
    }

    private Component createCostMessage(Currency currency, double price) {
        return Component.text("Your journey will cost ").append(currency.asDisplay(price));
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
    public boolean run(CommandContext commandContext, String... args) {
        CommandSource source = commandContext.getSource();
        Currency currency = TranslateCore.getCurrencyManager().getDefaultCurrency();
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
}

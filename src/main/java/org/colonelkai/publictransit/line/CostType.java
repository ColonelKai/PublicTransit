package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.lamda.TriFunction;

import java.util.List;

public enum CostType {
    PER_NODE((line, start, end) -> {
        List<Node> nodes = line.getNodes();
        int startIndex = nodes.indexOf(start);
        int endIndex = nodes.indexOf(end);
        return ((double) Math.abs(endIndex - startIndex)) * line.getCost();
    }),
    PER_STOP((line, start, end) -> {
        int amount = (int) line.getNodesBetween(start, end).stream().filter(node -> node.getNodeType() == NodeType.STOP).count();
        double cost = line.getCost();
        return amount * cost;
    }),
    FLAT_RATE((line, start, end) -> line.getCost());

    private final TriFunction<Line, Node, Node, Double> applier;

    CostType(TriFunction<Line, Node, Node, Double> applier) {
        this.applier = applier;
    }

    public double get(Line line, Node starting, Node ending) {
        return this.applier.apply(line, starting, ending);
    }

}

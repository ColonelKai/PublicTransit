package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.Buildable;
import org.easy.config.auto.annotations.ConfigConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line implements Buildable<LineBuilder, Line> {

    private final String identifier;
    private final String name;

    private final List<Node> nodes;

    private final int cost;
    private final CostType costType;

    private final boolean oneWay;
    private final boolean oneWayReversed;

    @ConfigConstructor
    private Line(String identifier, String name, List<Node> nodes, int cost, CostType costType, boolean oneWay, boolean oneWayReversed) {
        this.identifier = identifier;
        this.name = name;
        this.costType = costType;
        this.cost = cost;
        this.oneWay = oneWay;
        this.oneWayReversed = oneWayReversed;
        this.nodes = new ArrayList<>(nodes);
    }

    public Line(@NotNull LineBuilder builder) {
        this.identifier = Objects.requireNonNull(builder.identifier());
        this.name = Objects.requireNonNull(builder.name());
        this.nodes = builder.nodes();
        if (this.nodes.isEmpty()) {
            throw new IllegalStateException("No nodes specified");
        }
        this.cost = Objects.requireNonNull(builder.cost());
        this.costType = Objects.requireNonNull(builder.costType());
        this.oneWay = builder.isOneWay();
        this.oneWayReversed = builder.isOneWayReversed();
    }

    public List<Node> getAllNodesBetween(int start, int end) {
        return this.getNodes().subList(start, end);
    }

    public int getCost() {
        return cost;
    }

    public CostType getCostType() {
        return costType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(nodes);
    }

    public int getPrice(int start, int end) {
        switch (this.costType) {
            case FLAT_RATE -> {
                return this.cost;
            }
            case PER_NODE -> {
                return Math.abs(start - end);
            }
            case PER_STOP -> {
                List<Node> nodes = getAllNodesBetween(start, end);
                return (int) nodes.stream().filter(node -> node.getNodeType() == NodeType.STOP).count();
            }
        }
        throw new RuntimeException("Should be impossible to hit");
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public boolean isOneWayReversed() {
        return oneWayReversed;
    }

    @Override
    public LineBuilder toBuilder() {
        return new LineBuilder()
                .setIdentifier(this.identifier)
                .setName(this.name)
                .setNodes(this.getNodes())
                .setCostType(this.costType)
                .setCost(this.cost)
                .setOneWay(this.oneWay)
                .setOneWayReversed(this.oneWayReversed);
    }
}

package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.utils.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineBuilder implements Builder<LineBuilder, Line> {

    private Double cost;
    private CostType costType;
    private String identifier;
    private boolean isOneWay;
    private boolean isOneWayReversed;
    private String name;
    private List<NodeBuilder> nodes = new ArrayList<>();

    public LineBuilder addNodes(NodeBuilder... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
        return this;
    }

    @Override
    public Line build() {
        return new Line(this);
    }

    @Override
    public LineBuilder from(LineBuilder lineBuilder) {
        this.nodes = lineBuilder.nodes();
        this.cost = lineBuilder.cost();
        this.costType = lineBuilder.costType();
        this.name = lineBuilder.name();
        this.identifier = lineBuilder.identifier();
        this.isOneWay = lineBuilder.isOneWay();
        this.isOneWayReversed = lineBuilder.isOneWayReversed();
        return this;
    }

    public Double cost() {
        return this.cost;
    }

    public CostType costType() {
        return this.costType;
    }

    public String identifier() {
        return this.identifier;
    }

    public boolean isOneWay() {
        return this.isOneWay;
    }

    public LineBuilder setOneWay(boolean oneWay) {
        this.isOneWay = oneWay;
        return this;
    }

    public boolean isOneWayReversed() {
        return this.isOneWayReversed;
    }

    public LineBuilder setOneWayReversed(boolean oneWayReversed) {
        this.isOneWayReversed = oneWayReversed;
        return this;
    }

    public String name() {
        return this.name;
    }

    public List<NodeBuilder> nodes() {
        return this.nodes;
    }

    public LineBuilder removeNode(String nodeName) {
        this.nodes.stream().filter(node -> null != node.name()).filter(node -> node.name().equals(nodeName)).forEach(n -> this.nodes.remove(n));
        return this;
    }

    public LineBuilder setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public LineBuilder setCostType(CostType costType) {
        this.costType = costType;
        return this;
    }

    public LineBuilder setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public LineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LineBuilder setNodes(List<NodeBuilder> nodes) {
        this.nodes = nodes;
        return this;
    }
}

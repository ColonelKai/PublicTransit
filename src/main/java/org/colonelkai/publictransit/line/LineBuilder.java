package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineBuilder implements Builder<LineBuilder, Line> {

    private Integer cost;
    private CostType costType;
    private String identifier;
    private boolean isOneWay;
    private boolean isOneWayReversed;
    private String name;
    private List<Node> nodes = new ArrayList<>();

    public LineBuilder addNodes(Node... nodes) {
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

    public Integer cost() {
        return cost;
    }

    public CostType costType() {
        return costType;
    }

    public String identifier() {
        return identifier;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public LineBuilder setOneWay(boolean oneWay) {
        isOneWay = oneWay;
        return this;
    }

    public boolean isOneWayReversed() {
        return isOneWayReversed;
    }

    public LineBuilder setOneWayReversed(boolean oneWayReversed) {
        isOneWayReversed = oneWayReversed;
        return this;
    }

    public String name() {
        return name;
    }

    public List<Node> nodes() {
        return nodes;
    }

    public LineBuilder removeNode(String nodeName) {
        this.nodes.stream().filter(node -> node.getName().equals(nodeName)).forEach(n -> this.nodes.remove(n));
        return this;
    }

    public LineBuilder removeNode(@NotNull Node node) {
        this.nodes.remove(node);
        return this;
    }

    public LineBuilder setCost(int cost) {
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

    public LineBuilder setNodes(List<Node> nodes) {
        this.nodes = nodes;
        return this;
    }
}

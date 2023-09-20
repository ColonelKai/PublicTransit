package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.utils.Builder;
import org.core.adventureText.AText;
import org.core.world.position.impl.ExactPosition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineBuilder implements Builder<LineBuilder, Line> {

    private Double cost;
    private CostType costType;
    private String identifier;
    private LineDirection direction;
    private AText name;
    private boolean isBiDirectional;
    private List<NodeBuilder> nodes = new ArrayList<>();

    public LineBuilder addNodes(NodeBuilder... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
        return this;
    }

    public boolean isBiDirectional() {
        return this.isBiDirectional;
    }

    public LineBuilder setBiDirectional(boolean isBiDirectional) {
        this.isBiDirectional = isBiDirectional;
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
        this.direction = lineBuilder.direction();
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

    public LineDirection direction() {
        return this.direction;
    }

    public LineBuilder setDirection(LineDirection direction) {
        this.direction = direction;
        return this;
    }

    public AText name() {
        return this.name;
    }

    public List<NodeBuilder> nodes() {
        return this.nodes;
    }

    public LineBuilder removeNode(ExactPosition position) {
        this.nodes.stream().filter(node -> null != node.position()).filter(node -> node.position().equals(position)).forEach(n -> this.nodes.remove(n));
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

    public LineBuilder setName(AText name) {
        this.name = name;
        return this;
    }

    public LineBuilder setNodes(List<NodeBuilder> nodes) {
        this.nodes = nodes;
        return this;
    }
}

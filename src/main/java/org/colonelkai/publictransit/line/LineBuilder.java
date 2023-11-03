package org.colonelkai.publictransit.line;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.utils.Builder;
import org.core.world.position.impl.ExactPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineBuilder implements Builder<LineBuilder, Line> {

    private Double cost;
    private CostType costType;
    private LineDirection direction;
    private String identifier;
    private boolean isBiDirectional;
    private Component name;
    private List<NodeBuilder> nodes = new ArrayList<>();
    private Integer weight;

    public LineBuilder addNodes(NodeBuilder... nodes) {
        this.nodes.addAll(Arrays.asList(nodes));
        return this;
    }

    public LineBuilder addNodeAt(int position, NodeBuilder builder) {
        this.nodes.add(position, builder);
        return this;
    }

    public @Nullable Integer weight() {
        return this.weight;
    }

    public LineBuilder setWeight(@Nullable Integer weight) {
        this.weight = weight;
        return this;
    }

    public LineBuilder setDefaultWeight() {
        return setWeight(null);
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

    public LineDirection direction() {
        return this.direction;
    }

    public String identifier() {
        return this.identifier;
    }

    public boolean isBiDirectional() {
        return this.isBiDirectional;
    }

    public LineBuilder setBiDirectional(boolean isBiDirectional) {
        this.isBiDirectional = isBiDirectional;
        return this;
    }

    public Component name() {
        return this.name;
    }

    public List<NodeBuilder> nodes() {
        return this.nodes;
    }

    public LineBuilder removeNode(@NotNull ExactPosition position) {
        this.nodes.stream().filter(node -> null != node.position()).filter(node -> node.position().equals(position)).forEach(n -> this.nodes.remove(n));
        return this;
    }

    public LineBuilder removeNode(@NotNull Node node) {
        this.nodes.remove(node);
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

    public LineBuilder setDirection(LineDirection direction) {
        this.direction = direction;
        return this;
    }

    public LineBuilder setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public LineBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    public LineBuilder setNodes(List<NodeBuilder> nodes) {
        this.nodes = nodes;
        return this;
    }
}

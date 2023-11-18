package org.colonelkai.publictransit.line;

import net.kyori.adventure.text.Component;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.options.CommandOption;
import org.colonelkai.publictransit.utils.Builder;
import org.core.world.position.Positionable;
import org.core.world.position.impl.ExactPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LineBuilder implements Builder<LineBuilder, Line> {

    private final List<NodeBuilder> nodes = new ArrayList<>();
    private Double cost;
    private CostType costType;
    private LineDirection direction;
    private String identifier;
    private boolean isBiDirectional;
    private Component name;
    private Integer weight;

    public LineBuilder addNodeAt(int position, NodeBuilder builder) {
        this.nodes.add(position, builder);
        return this;
    }

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
        this.nodes.addAll(lineBuilder.nodes());
        this.cost = lineBuilder.cost();
        this.costType = lineBuilder.costType();
        this.name = lineBuilder.name();
        this.identifier = lineBuilder.identifier();
        this.direction = lineBuilder.direction();
        return this;
    }

    @CommandOption
    public Double cost() {
        return this.cost;
    }

    @CommandOption
    public CostType costType() {
        return this.costType;
    }

    @CommandOption
    public LineDirection direction() {
        return this.direction;
    }

    public String identifier() {
        return this.identifier;
    }

    @CommandOption(setter = "setBiDirectional")
    public boolean isBiDirectional() {
        return this.isBiDirectional;
    }

    public LineBuilder setBiDirectional(boolean isBiDirectional) {
        this.isBiDirectional = isBiDirectional;
        return this;
    }

    @CommandOption
    public Component name() {
        return this.name;
    }

    public List<NodeBuilder> nodes() {
        return this.nodes;
    }

    public LineBuilder removeNode(@NotNull ExactPosition position) {
        var toRemove = this.nodes.stream().filter(node -> null != node.position()).filter(node -> node.position().equals(position)).toList();
        this.nodes.removeAll(toRemove);
        return this;
    }

    public LineBuilder removeNode(@NotNull Positionable<ExactPosition> node) {
        return this.removeNode(node.getPosition());
    }

    public LineBuilder setCost(double cost) {
        this.cost = cost;
        return this;
    }

    public LineBuilder setCostType(CostType costType) {
        this.costType = costType;
        return this;
    }

    public LineBuilder setDefaultWeight() {
        return setWeight(null);
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

    public LineBuilder setNodes(Collection<NodeBuilder> nodes) {
        this.nodes.clear();
        this.nodes.addAll(nodes);
        return this;
    }

    public LineBuilder setWeight(@Nullable Integer weight) {
        this.weight = weight;
        return this;
    }

    @CommandOption
    public @Nullable Integer weight() {
        return this.weight;
    }
}

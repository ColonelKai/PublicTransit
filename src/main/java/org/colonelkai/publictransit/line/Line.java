package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.NodeManager;
import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.Buildable;
import org.colonelkai.publictransit.utils.Savable;
import org.colonelkai.publictransit.utils.serializers.Serializers;
import org.core.TranslateCore;
import org.core.adventureText.AText;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.easy.config.auto.annotations.ConfigConstructor;
import org.easy.config.auto.annotations.ConfigList;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Line implements Buildable<LineBuilder, Line>, Savable {

    private final String identifier;
    private final String name;

    @ConfigList(ofType = Node.class)
    private final List<Node> nodes;

    private final double cost;
    private final CostType costType;

    private final LineDirection direction;

    @ConfigConstructor
    private Line(String identifier, String name, List<Node> nodes, double cost, CostType costType, LineDirection direction) {
        this.identifier = identifier;
        this.name = name;
        this.costType = costType;
        this.cost = cost;
        this.direction = direction;
        this.nodes = new ArrayList<>(nodes);
        this.validate(this.nodes);
    }

    Line(@NotNull LineBuilder builder) {
        this.identifier = Objects.requireNonNull(builder.identifier());
        this.name = Objects.requireNonNullElse((null == builder.name()) ? null : builder.name().toPlain(), this.identifier); //this needs fixing
        this.nodes = builder.nodes().stream().map(NodeBuilder::build).toList();
        this.cost = Objects.requireNonNull(builder.cost());
        this.costType = Objects.requireNonNull(builder.costType());
        this.direction = Objects.requireNonNull(builder.direction());
        this.validate(this.nodes);
    }

    private void validate(Collection<Node> nodes) {
        Collection<Node> uniqueTest = new HashSet<>(nodes);
        if (uniqueTest.size() != nodes.size()) {
            throw new IllegalStateException("Two or more nodes have the same position");
        }
    }

    public boolean isActive() {
        return 2 <= this.nodes.stream().filter(node -> NodeType.STOP == node.getNodeType()).count();
    }

    public List<Node> getNodesBetween(Node start, Node end) {
        int startIndex = this.nodes.indexOf(start);
        int endIndex = this.nodes.indexOf(end);
        return this.nodes.subList(Math.min(startIndex, endIndex), Math.max(startIndex, endIndex));
    }

    public Optional<Node> getNextNode(Node node) {
        return this.getNextNode(this.getNodes().indexOf(node));
    }

    public Optional<Node> getNextNode(int index) {
        return this.direction.getNextNode(index, this.nodes);
    }

    public double getCost() {
        return this.cost;
    }

    public CostType getCostType() {
        return this.costType;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public AText getName() {
        return AText.ofPlain(this.name); //needs fixing
    }

    public List<Node> getNodes() {
        return Collections.unmodifiableList(this.nodes);
    }

    public void addNode(Node node) {
        Collection<Node> nodes = new ArrayList<>(this.nodes);
        nodes.add(node);
        this.validate(nodes);
        this.nodes.add(node);
    }

    @Override
    public File defaultFile() {
        return new File(NodeManager.LINES_DATA_PATH, this.identifier);
    }

    @Override
    public void save(File file) throws Exception {
        PublicTransit.getPlugin().getNodeManager().save(this, file);
    }

    public double getPrice() {
        return this.getPrice(this.nodes.get(0), this.nodes.get(this.nodes.size() - 1));
    }

    public double getPrice(@NotNull Node start, @NotNull Node end) {
        return this.costType.get(this, start, end);
    }

    public LineDirection getDirection() {
        return this.direction;
    }

    @Override
    public LineBuilder toBuilder() {
        return new LineBuilder()
                .setIdentifier(this.identifier)
                .setName(AText.ofPlain(this.name))
                .setNodes(this.getNodes().stream().map(Node::toBuilder).toList())
                .setCostType(this.costType)
                .setCost(this.cost)
                .setDirection(this.direction);
    }

    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Line line)) {
            return false;
        }
        return line.identifier.equalsIgnoreCase(this.identifier);
    }
}

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
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.easy.config.auto.annotations.ConfigConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Line implements Buildable<LineBuilder, Line>, Savable {

    private final String identifier;
    private final String name;

    private final List<Node> nodes;

    private final double cost;
    private final CostType costType;

    // this is to indicate that a player may only travel one direction in the line.
    private final boolean oneWay;
    // normally, when oneWay is true, the player will only be able to travel in an increasing order of index. So for example, 2 to 3 but not 3 to 2.
    // oneWayReversed makes it so they can only travel in reverse, aka, in a decreasing order of index.
    private final boolean oneWayReversed;

    @ConfigConstructor
    private Line(String identifier, String name, List<Node> nodes, double cost, CostType costType, boolean oneWay, boolean oneWayReversed) {
        this.identifier = identifier;
        this.name = name;
        this.costType = costType;
        this.cost = cost;
        this.oneWay = oneWay;
        this.oneWayReversed = oneWayReversed;
        this.nodes = new ArrayList<>(nodes);
    }

    Line(@NotNull LineBuilder builder) {
        this.identifier = Objects.requireNonNull(builder.identifier());
        this.name = Objects.requireNonNullElse(builder.name(), this.identifier);
        this.nodes = builder.nodes().stream().map(NodeBuilder::build).toList();
        this.cost = Objects.requireNonNull(builder.cost());
        this.costType = Objects.requireNonNull(builder.costType());
        this.oneWay = builder.isOneWay();
        this.oneWayReversed = builder.isOneWayReversed();
    }


//    We don't care about this, two nodes *can* have the same name!! Or literally be the same...

//    private void validateNodes() {
//        if (2 > this.nodes.size()) {
//            throw new IllegalStateException("Requires two or more nodes");
//        }
//        Collection<Node> uniqueTest = new HashSet<>(this.nodes);
//        if (uniqueTest.size() != this.nodes.size()) {
//            throw new IllegalStateException("Two or more nodes have the same name");
//        }
//    }

    public List<Node> getNodesBetween(Node start, Node end) {
        int startIndex = this.nodes.indexOf(start);
        int endIndex = this.nodes.indexOf(end);
        return this.getNodes().subList(startIndex, endIndex);
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

    public String getName() {
        return this.name;
    }

    public List<Node> getNodes() {
        return this.getNodes(false);
    }

    public List<Node> getNodes(boolean reverse) {
        List<Node> nodes = new ArrayList<>(this.nodes);

// I think you may be misunderstanding this... Read above for the notes.
//        if (this.oneWayReversed != reverse) {
//            Collections.reverse(nodes);
//        }
        return Collections.unmodifiableList(nodes);
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

    public boolean isOneWay() {
        return this.oneWay;
    }

    public boolean isOneWayReversed() {
        return this.oneWayReversed;
    }

    @Override
    public LineBuilder toBuilder() {
        return new LineBuilder()
                .setIdentifier(this.identifier)
                .setName(this.name)
                .setNodes(this.getNodes().stream().map(Node::toBuilder).toList())
                .setCostType(this.costType)
                .setCost(this.cost)
                .setOneWay(this.oneWay)
                .setOneWayReversed(this.oneWayReversed);
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

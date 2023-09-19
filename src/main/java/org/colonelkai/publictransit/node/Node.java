package org.colonelkai.publictransit.node;

import org.colonelkai.publictransit.utils.Buildable;
import org.colonelkai.publictransit.utils.serializers.NodeTypeSerializer;
import org.colonelkai.publictransit.utils.serializers.PositionSerializer;
import org.core.entity.living.human.player.Player;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.easy.config.auto.annotations.ConfigConstructor;
import org.easy.config.auto.annotations.ConfigField;

import java.util.Objects;
import java.util.OptionalInt;

public class Node implements Buildable<NodeBuilder, Node> {

    @ConfigField(serializer = PositionSerializer.class)
    private final ExactPosition location;

    private final String name;
    private final NodeType nodeType;
    @ConfigField(optional = true)
    private final Integer time;

    @ConfigConstructor
    private Node(Position<?> position, String name, NodeType nodeType, Integer time) {
        this.location = Objects.requireNonNull(position).toExactPosition();
        this.name = Objects.requireNonNull(name);
        this.nodeType = Objects.requireNonNull(nodeType);
        this.time = time;
        validateNode();
    }

    public Node(NodeBuilder builder) {
        this.location = Objects.requireNonNull(builder.position()).toExactPosition();
        this.nodeType = Objects.requireNonNull(builder.type());
        this.name = Objects.requireNonNull(builder.name());
        this.time = builder.time();
        validateNode();
    }

    private void validateNode() {
        if (this.name.contains(" ")) {
            throw new IllegalArgumentException("Name cannot have spaces included");
        }
    }

    public String getName() {
        return name;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public ExactPosition getPosition() {
        return location;
    }

    public OptionalInt getTime() {
        if (this.time == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.time);
    }

    public boolean isPlayerWithin(Player<?> player) {
        ExactPosition playerLocation = player.getPosition();
        if (!playerLocation.getWorld().equals(this.location.getWorld())) {
            return false;
        }
        double distance = playerLocation.getPosition().distanceSquared(this.location.getPosition());
        return distance < 10;

    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node node)) {
            return false;
        }
        return node.name.equalsIgnoreCase(this.name);
    }

    @Override
    public NodeBuilder toBuilder() {
        return new NodeBuilder().setPosition(this.location).setTime(this.time).setType(this.nodeType).setName(this.name);
    }
}

package org.colonelkai.publictransit.node;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.PublicTransitConfigNodes;
import org.colonelkai.publictransit.utils.Buildable;
import org.colonelkai.publictransit.utils.serializers.NodeTypeSerializer;
import org.colonelkai.publictransit.utils.serializers.PositionSerializer;
import org.core.entity.Entity;
import org.core.entity.living.human.player.Player;
import org.core.world.position.Positionable;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.easy.config.auto.annotations.ConfigConstructor;
import org.easy.config.auto.annotations.ConfigField;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

public class Node implements Buildable<NodeBuilder, Node> {

    @ConfigField(serializer = PositionSerializer.class)
    private final ExactPosition location;

    @ConfigField(optional = true)
    private final @Nullable String name;
    private final NodeType nodeType;
    @ConfigField(optional = true)
    private final @Nullable Integer time;

    @ConfigConstructor
    Node(Position<?> position, @Nullable String name, NodeType nodeType, @Nullable Integer time) {
        this.location = position.toExactPosition();
        this.name = name;
        this.nodeType = nodeType;
        this.time = time;
        this.validate();
    }

    public Node(NodeBuilder builder) {
        this.location = Objects.requireNonNull(builder.position()).toExactPosition();
        this.nodeType = Objects.requireNonNull(builder.type());
        this.name = builder.name();
        this.time = builder.time();
        this.validate();
    }

    private void validate() {
        if ((null == this.name) && (NodeType.STOP == this.nodeType)) {
            throw new RuntimeException("A stop must have a name specified");
        }
    }

    public Optional<String> getName() {
        return Optional.ofNullable(this.name);
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    public ExactPosition getPosition() {
        return this.location;
    }

    public OptionalInt getTime() {
        if (null == this.time) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.time);
    }

    public boolean isWithin(Positionable<? extends Position<?>> positionable) {
        return this.isWithin(positionable.getPosition());
    }

    public boolean isWithin(Position<?> position) {
        if (!position.getWorld().equals(this.location.getWorld())) {
            return false;
        }
        double distance = position.getPosition().distanceSquared(this.location.getPosition());
        double configDistance = PublicTransitConfigNodes.PLAYER_DISTANCE_FROM_NODE.get();
        return distance < configDistance;

    }

    @Override
    public int hashCode() {
        return this.location.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node node)) {
            return false;
        }
        return node.location.equals(this.location);
    }

    @Override
    public NodeBuilder toBuilder() {
        return new NodeBuilder().setPosition(this.location).setTime(this.time).setType(this.nodeType).setName(this.name);
    }
}

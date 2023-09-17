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
    private final SyncExactPosition location;

    private final String name;
    private final NodeType nodeType;
    @ConfigField(optional = true)
    private final Integer time;

    @ConfigConstructor
    Node(Position<?> position, String name, NodeType nodeType, Integer time){
        this.location = Position.toSync(Position.toExact(position));
        this.name = name;
        this.nodeType = nodeType;
        this.time = time;
    }

    public Node(NodeBuilder builder) {
        this.location = Position.toSync(Position.toExact(Objects.requireNonNull(builder.position())));
        this.nodeType = Objects.requireNonNull(builder.type());
        this.name = Objects.requireNonNull(builder.name());
        this.time = builder.time();
    }

    public String getName() {
        return name;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public SyncExactPosition getPosition() {
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
    public NodeBuilder toBuilder() {
        return new NodeBuilder().setPosition(this.location).setTime(this.time).setType(this.nodeType).setName(this.name);
    }
}

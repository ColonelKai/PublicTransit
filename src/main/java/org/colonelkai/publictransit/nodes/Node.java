package org.colonelkai.publictransit.nodes;

import org.core.entity.living.human.player.Player;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.Position;
import org.core.world.position.impl.sync.SyncExactPosition;

import java.util.OptionalInt;

public class Node {
    private SyncExactPosition location;
    private NodeTypes nodeType;
    private Integer time;
    private String name;

    public Node(Position<?> location, NodeTypes nodeType, String name) {
        this.location = Position.toSync(Position.toExact(location));
        this.nodeType = nodeType;
        this.name = name;
    }

    public boolean isPlayerWithin(Player<?> player) {
        ExactPosition playerLocation = player.getPosition();
        if (!playerLocation.getWorld().equals(this.location.getWorld())) {
            return false;
        }
        double distance = playerLocation.getPosition().distanceSquared(this.location.getPosition());
        return distance < 10;

    }

    public SyncExactPosition getPosition() {
        return location;
    }

    // GETTER-SETTERS
    public void setLocation(Position<?> location) {
        this.location = Position.toSync(Position.toExact(location));
    }

    public NodeTypes getNodeType() {
        return nodeType;
    }

    public void setNodeType(NodeTypes nodeType) {
        this.nodeType = nodeType;
    }

    public OptionalInt getTime() {
        if (this.time == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(this.time);
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void removeTime() {
        this.time = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package org.colonelkai.publictransit.nodes;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Optional;

public class Node {
    private Location location;
    private NodeTypes nodeType;
    private Optional<Integer> time;
    private String name;

    public Node(Location location, NodeTypes nodeType, String name) {
        this.location = location;
        this.nodeType = nodeType;
        this.time = Optional.empty();
        this.name = name;
    }

    public boolean isPlayerWithin(Player player) {
        Location playerLocation = player.getLocation();

        return
                Objects.equals(playerLocation.getWorld(), this.location.getWorld())
                        &&
                        playerLocation.distance(this.location) < 10;

    }







    // GETTER-SETTERS
    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setNodeType(NodeTypes nodeType) {
        this.nodeType = nodeType;
    }

    public NodeTypes getNodeType() {
        return nodeType;
    }

    public void setTime(Integer time) {
        this.time = Optional.of(time);
    }

    public Optional<Integer> getTime() {
        return time;
    }

    public void setTime(Optional<Integer> time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

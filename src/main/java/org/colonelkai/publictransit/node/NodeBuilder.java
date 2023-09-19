package org.colonelkai.publictransit.node;

import org.colonelkai.publictransit.utils.Builder;
import org.core.world.position.impl.Position;

public class NodeBuilder implements Builder<NodeBuilder, Node> {

    private Position<?> position;
    private String name;
    private Integer time;
    private NodeType type;

    @Override
    public Node build() {
        return new Node(this);
    }

    @Override
    public NodeBuilder from(NodeBuilder nodeBuilder) {
        this.name = nodeBuilder.name();
        this.time = nodeBuilder.time();
        this.type = nodeBuilder.type();
        this.position = nodeBuilder.position();
        return this;
    }

    public Position<?> position() {
        return position;
    }

    public String name() {
        return name;
    }

    public NodeBuilder setPosition(Position<?> location) {
        this.position = location;
        return this;
    }

    public NodeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NodeBuilder setTime(Integer time) {
        this.time = time;
        return this;
    }

    public NodeBuilder setType(NodeType type) {
        this.type = type;
        return this;
    }

    public Integer time() {
        return time;
    }

    public NodeType type() {
        return type;
    }
}

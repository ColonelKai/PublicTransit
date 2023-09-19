package org.colonelkai.publictransit.utils.serializers;

import org.colonelkai.publictransit.node.NodeType;
import org.easy.config.Serializer;

public class NodeTypeSerializer implements Serializer.Text<NodeType> {
    @Override
    public String serialize(NodeType value) {
        return value.name();
    }

    @Override
    public NodeType deserialize(String type) throws IllegalArgumentException {
        return NodeType.valueOf(type.toUpperCase());
    }

    @Override
    public Class<?> ofType() {
        return NodeType.class;
    }
}

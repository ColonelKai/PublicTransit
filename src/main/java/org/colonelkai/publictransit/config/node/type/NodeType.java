package org.colonelkai.publictransit.config.node.type;

import org.colonelkai.publictransit.config.node.ConfigNodeGetter;
import org.colonelkai.publictransit.config.node.ConfigNodeSetter;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;

import java.util.Optional;

public interface NodeType {

    static <T extends Enum<T>> ConfigNodeGetter<T> enumGetter(Class<T> type) {
        return (stream, node) -> stream.getString(node).map(v -> Enum.valueOf(type, v.toUpperCase()));
    }

    static <T extends Enum<T>> ConfigNodeSetter<T> enumSetter(Class<T> type) {
        return (stream, node, t) -> stream.set(node, t.name());
    }

}

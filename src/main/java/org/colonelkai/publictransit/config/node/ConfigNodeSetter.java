package org.colonelkai.publictransit.config.node;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;

public interface ConfigNodeSetter<Value> {

    void set(ConfigurationStream stream, ConfigurationNode node, Value value);

    default void set(ConfigurationStream stream, String[] path, Value value) {
        this.set(stream, new ConfigurationNode(path), value);
    }


}

package org.colonelkai.publictransit.config.node;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;

import java.util.Optional;

public interface ConfigNodeGetter<Value> {

    Optional<Value> get(ConfigurationStream stream, ConfigurationNode node);

    default Optional<Value> get(ConfigurationStream stream, String... path) {
        return this.get(stream, new ConfigurationNode(path));
    }

}

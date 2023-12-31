package org.colonelkai.publictransit.config.node;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConfigNode<V> {

    @NotNull ConfigurationNode getPath();

    @NotNull V defaultValue();

    @NotNull V get();

    void reset();

    void setValue(@NotNull ConfigurationStream stream, @NotNull V value);

}

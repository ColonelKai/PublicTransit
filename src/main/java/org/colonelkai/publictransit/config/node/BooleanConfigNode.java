package org.colonelkai.publictransit.config.node;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class BooleanConfigNode extends AbstractConfigNode<Boolean> {
    public BooleanConfigNode(ConfigurationNode path, Boolean defaultValue) {
        super(path, defaultValue);
    }

    @Override
    protected @NotNull Optional<Boolean> get(@NotNull ConfigurationStream stream) {
        return stream.getBoolean(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull Boolean value) {
        stream.set(this.getPath(), value);
    }
}

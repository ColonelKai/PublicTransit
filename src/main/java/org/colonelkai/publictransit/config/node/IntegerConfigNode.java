package org.colonelkai.publictransit.config.node;

import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class IntegerConfigNode extends AbstractConfigNode<Integer> {

    public IntegerConfigNode(ConfigurationNode path, int defaultValue) {
        super(path, defaultValue);
    }

    @Override
    protected @NotNull Optional<Integer> get(@NotNull ConfigurationStream stream) {
        return stream.getInteger(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull Integer value) {
        stream.set(this.getPath(), value);
    }
}

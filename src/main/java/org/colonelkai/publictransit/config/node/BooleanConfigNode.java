package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class BooleanConfigNode extends AbstractConfigNode<Boolean> {
    public BooleanConfigNode(ConfigurationNode path, Boolean defaultValue, Supplier<Config> config) {
        super(path, defaultValue, config);
    }

    @Override
    protected @NotNull Optional<Boolean> getRaw(@NotNull ConfigurationStream stream) {
        return stream.getBoolean(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull Boolean value) {
        stream.set(this.getPath(), value);
    }
}

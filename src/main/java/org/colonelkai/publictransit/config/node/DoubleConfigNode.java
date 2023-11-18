package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class DoubleConfigNode extends AbstractConfigNode<Double> {

    public DoubleConfigNode(ConfigurationNode path, double defaultValue, Supplier<Config> config) {
        super(path, defaultValue, config);
    }

    @Override
    protected @NotNull Optional<Double> getRaw(@NotNull ConfigurationStream stream) {
        return stream.getDouble(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull Double value) {
        stream.set(this.getPath(), value);
    }
}

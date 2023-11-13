package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class IntegerConfigNode extends AbstractConfigNode<Integer> {

    public IntegerConfigNode(ConfigurationNode path, int defaultValue, Supplier<Config> config) {
        super(path, defaultValue, config);
    }

    public IntegerConfigNode(ConfigurationNode path, Integer defaultValue, Supplier<Config> config, Function<Integer, Integer> parseFunc) {
        super(path, defaultValue, config, parseFunc);
    }

    @Override
    protected @NotNull Optional<Integer> getRaw(@NotNull ConfigurationStream stream) {
        return stream.getInteger(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull Integer value) {
        stream.set(this.getPath(), value);
    }
}

package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class StringConfigNode extends AbstractConfigNode<String> {
    public StringConfigNode(ConfigurationNode path, String defaultValue, Supplier<Config> config) {
        super(path, defaultValue, config);
    }

    @Override
    protected @NotNull Optional<String> getRaw(@NotNull ConfigurationStream stream) {
        return stream.getString(this.getPath());
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull String value) {
        stream.set(this.getPath(), value);
    }
}

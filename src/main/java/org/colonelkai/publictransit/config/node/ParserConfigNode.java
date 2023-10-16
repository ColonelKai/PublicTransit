package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class ParserConfigNode<P, T> extends AbstractConfigNode<T> {

    public ParserConfigNode(ConfigurationNode.KnownParser.SingleKnown<T> path, T defaultValue, Config config) {
        super(path, defaultValue, config);
    }

    @Override
    @NotNull
    public ConfigurationNode.KnownParser.SingleKnown<T> getPath() {
        return (ConfigurationNode.KnownParser.SingleKnown<T>) super.getPath();
    }

    @Override
    protected @NotNull Optional<T> getRaw(@NotNull ConfigurationStream stream) {
        return stream.parse(getPath());
    }

    @Override
    public void set(@NotNull ConfigurationStream stream, @NotNull T value) {
        stream.set(getPath(), value);
    }
}

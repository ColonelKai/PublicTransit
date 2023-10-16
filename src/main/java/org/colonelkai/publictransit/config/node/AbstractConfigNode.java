package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

abstract class AbstractConfigNode<T> implements ConfigNode<T> {

    private final ConfigurationNode path;
    private final T defaultValue;
    private T lastKnown;

    private Config config;

    Function<T, T> parseFunc;

    AbstractConfigNode(ConfigurationNode path, T defaultValue, Config config, Function <T, T> parseFunc) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.config = config;
        this.parseFunc = parseFunc;
    }

    protected AbstractConfigNode(ConfigurationNode path, T defaultValue, Config config) {
        this.path = path;
        this.defaultValue = defaultValue;
        this.parseFunc = (t->t); // if no parseFunc is given, just return optional of.
        this.config = config;
    }

    protected abstract @NotNull Optional<T> getRaw(@NotNull ConfigurationStream stream);

    protected abstract void set(@NotNull ConfigurationStream stream, @NotNull T value);

    @Override
    public @NotNull ConfigurationNode getPath() {
        return this.path;
    }

    @Override
    public @NotNull T defaultValue() {
        return this.defaultValue;
    }

    @Override
    public @NotNull T getRaw() {
        ConfigurationStream stream = this.config.getFile();

        if (this.lastKnown == null) {
            this.lastKnown = this.getRaw(stream).orElseThrow(() -> new IllegalStateException("Unable to read '" + String.join(";", getPath().getPath()) + "'"));
        }
        return this.lastKnown;
    }

    @Override
    public void reset() {
        this.lastKnown = null;
    }

    @Override
    public void setValue(@NotNull ConfigurationStream stream, @NotNull T value) {
        this.lastKnown = value;
        this.set(stream, value);
        stream.save();
    }

    @Override
    public @NotNull T get() {
        return this.parseFunc.apply(this.getRaw());
    }
}

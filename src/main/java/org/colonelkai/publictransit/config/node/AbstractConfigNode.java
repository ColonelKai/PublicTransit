package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class AbstractConfigNode<T> implements ConfigNode<T> {

    private final ConfigurationNode path;
    private final T defaultValue;
    private final Function<T, T> parseFunc;
    private final Supplier<Config> config;
    private T lastKnown;

    AbstractConfigNode(ConfigurationNode path, T defaultValue, Supplier<Config> config, Function<T, T> parseFunc) {
        this.path = Objects.requireNonNull(path, "Path is missing");
        this.defaultValue = Objects.requireNonNull(defaultValue, "default value is missing");
        this.config = Objects.requireNonNull(config, "config is missing");
        this.parseFunc = Objects.requireNonNull(parseFunc, "Parse function is missing");
    }

    AbstractConfigNode(ConfigurationNode path, T defaultValue, Supplier<Config> config) {
        this(path, defaultValue, config, t -> t);
    }

    @Override
    public @NotNull ConfigurationNode getPath() {
        return this.path;
    }

    @Override
    public @NotNull T defaultValue() {
        return this.defaultValue;
    }

    @Override
    public @NotNull T get() {
        return this.parseFunc.apply(this.getRaw());
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

    protected abstract @NotNull Optional<T> getRaw(@NotNull ConfigurationStream stream);

    public @NotNull T getRaw() {
        ConfigurationStream.ConfigurationFile stream = this.config.get().getFile();

        if (this.lastKnown == null) {
            this.lastKnown = this
                    .getRaw(stream)
                    .orElseThrow(() -> new IllegalStateException(
                            "Unable to read '" + String.join(";", getPath().getPath()) + "' " + "in " + stream.getFile().getPath()));
        }
        return this.lastKnown;
    }

    protected abstract void set(@NotNull ConfigurationStream stream, @NotNull T value);
}

package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

public class ConfigNode<Apply> implements Supplier<Apply> {

    private final Supplier<Config> stream;
    private final ConfigNodeSetter<Apply> set;
    private final ConfigNodeGetter<Apply> getter;
    private final Supplier<Apply> defaultValue;
    private final String[] path;
    private Apply cached;

    ConfigNode(ConfigNodeBuilder builder) {
        this.stream = Objects.requireNonNull(builder.config);
        this.set = (ConfigNodeSetter<Apply>) Objects.requireNonNull(builder.setter);
        this.getter = (ConfigNodeGetter<Apply>) Objects.requireNonNull(builder.getter);
        this.defaultValue = (Supplier<Apply>) Objects.requireNonNull(builder.defaultValue);
        this.path = Objects.requireNonNull(builder.path);
        if (0 == this.path.length) {
            throw new IllegalArgumentException("Path must be specified");
        }
    }

    public Config getConfig() {
        return this.stream.get();
    }

    @Override
    public @NotNull Apply get() {
        if (null != this.cached) {
            return this.cached;
        }
        Optional<Apply> opValue = this.getter.get(this.stream.get().getFile(), this.path);
        opValue.ifPresent(value -> this.cached = value);
        return opValue.orElseGet(this.defaultValue);
    }

    public void set(@NotNull Apply set) {
        this.set.set(this.stream.get().getFile(), this.path, set);
        //for caching value .... probably better way to do this
        this.getter.get(this.stream.get().getFile(), this.path).ifPresent(value -> this.cached = value);
    }

    public void setDefault() {
        this.set(this.defaultValue.get());
    }

    public void clearCache() {
        this.cached = null;
    }
}

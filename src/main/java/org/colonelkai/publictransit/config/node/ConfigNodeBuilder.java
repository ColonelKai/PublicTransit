package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.colonelkai.publictransit.config.node.type.NodeType;
import java.util.function.Supplier;

public class ConfigNodeBuilder {

    ConfigNodeGetter<?> getter;
    ConfigNodeSetter<?> setter;
    String[] path;
    Supplier<?> defaultValue;
    Supplier<Config> config;

    public <Apply> ConfigNode<Apply> build() {
        return new ConfigNode<>(this);
    }

    public ConfigNodeBuilder config(Supplier<Config> config) {
        this.config = config;
        return this;
    }

    public ConfigNodeBuilder type(Class<? extends NodeType> typeClass) {
        try {
            this.getter = (ConfigNodeGetter<?>) typeClass.getField("GETTER").get(null);
            this.setter = (ConfigNodeSetter<?>) typeClass.getField("SETTER").get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public ConfigNodeBuilder read(ConfigNodeGetter<?> getter) {
        this.getter = getter;
        return this;
    }

    public ConfigNodeBuilder set(ConfigNodeSetter<?> setter) {
        this.setter = setter;
        return this;
    }

    @Deprecated //used to warn developers to insert a path
    public ConfigNodeBuilder setPath() {
        throw new RuntimeException("path required");
    }

    public ConfigNodeBuilder setPath(String... path) {
        this.path = path;
        return this;
    }

    public ConfigNodeBuilder setDefault(Supplier<?> defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ConfigNodeBuilder setDefault(Object defaultValue) {
        return setDefault(() -> defaultValue);
    }
}

package org.colonelkai.publictransit.config.node;

import org.colonelkai.publictransit.config.Config;
import org.colonelkai.publictransit.line.CostType;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CostTypeConfigNode extends AbstractConfigNode<CostType> {

    public CostTypeConfigNode(ConfigurationNode path, CostType defaultValue, Config config) {
        super(path, defaultValue, config);
    }

    @Override
    protected @NotNull Optional<CostType> getRaw(@NotNull ConfigurationStream stream) {
        return stream.getString(this.getPath()).map(CostType::valueOf); // is this about to anger mose?
    }

    @Override
    protected void set(@NotNull ConfigurationStream stream, @NotNull CostType value) {
        stream.set(this.getPath(), value.toString());
    }
}

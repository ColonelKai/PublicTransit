package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.config.node.ConfigNode;
import org.colonelkai.publictransit.config.node.ConfigNodeBuilder;
import org.colonelkai.publictransit.config.node.type.DoubleNodeType;
import org.core.config.ConfigurationNode;

public interface PublicTransitConfigNodes {

    ConfigNode<Double> PLAYER_DISTANCE_FROM_NODE = new ConfigNodeBuilder()
            .type(DoubleNodeType.class)
            .setDefault(10)
            .setPath("node", "distance", "player")
            .build();
}

package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.config.node.DoubleConfigNode;
import org.core.config.ConfigurationNode;

interface PublicTransitConfigNodes {

    DoubleConfigNode PLAYER_DISTANCE_FROM_NODE = new DoubleConfigNode(new ConfigurationNode("node", "distance", "player"), 10);

}

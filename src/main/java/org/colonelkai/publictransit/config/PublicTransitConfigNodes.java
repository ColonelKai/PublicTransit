package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.config.node.BooleanConfigNode;
import org.colonelkai.publictransit.config.node.DoubleConfigNode;
import org.colonelkai.publictransit.config.node.IntegerConfigNode;
import org.core.config.ConfigurationNode;

interface PublicTransitConfigNodes {

    DoubleConfigNode PLAYER_DISTANCE_FROM_NODE = new DoubleConfigNode(new ConfigurationNode("node", "distance", "player"), 10);
    IntegerConfigNode WEIGHT_MAXIMUM_DEFAULT = new IntegerConfigNode(new ConfigurationNode("line", "weight", "default"), 500);
    BooleanConfigNode WEIGHT_ENABLED = new BooleanConfigNode(new ConfigurationNode("line", "weight", "enabled"), true);

}

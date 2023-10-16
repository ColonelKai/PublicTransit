package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.node.*;
import org.colonelkai.publictransit.line.CostType;
import org.core.config.ConfigurationNode;

interface PublicTransitConfigNodes {


    // General Settings
    // Max distance a player needs to be from a node to interact with it
    DoubleConfigNode PLAYER_DISTANCE_FROM_NODE = new DoubleConfigNode(new ConfigurationNode(
            "node", "distance", "player"), 10, PublicTransit.getPlugin().getConfig());

    // Node Default Settings
    DoubleConfigNode DEFAULT_NODE_PRICE = new DoubleConfigNode(new ConfigurationNode(
            "node", "default", "price"), 100, PublicTransit.getPlugin().getConfig());
    DoubleConfigNode DEFAULT_NODE_TIME = new DoubleConfigNode(new ConfigurationNode(
            "node", "default", "time"), 15, PublicTransit.getPlugin().getConfig());

    // Line Default Settings

    CostTypeConfigNode DEFAULT_LINE_COST_TYPE = new CostTypeConfigNode(new ConfigurationNode(
            "line", "default", "costtype"), CostType.FLAT_RATE, PublicTransit.getPlugin().getConfig());

    // Economy Settings

    BooleanConfigNode USE_DEPOSIT_ACCOUNT = new BooleanConfigNode(new ConfigurationNode(
            "economy", "depositaccount", "use"), false, PublicTransit.getPlugin().getConfig());
    StringConfigNode DEPOSIT_ACCOUNT = new StringConfigNode(new ConfigurationNode(
            "economy", "depositaccount", "name"), "Government", PublicTransit.getPlugin().getConfig());
}

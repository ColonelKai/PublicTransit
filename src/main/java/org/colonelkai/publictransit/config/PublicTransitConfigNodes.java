package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.node.*;
import org.colonelkai.publictransit.line.CostType;
import org.core.config.ConfigurationNode;

interface PublicTransitConfigNodes {

    // CONFIG FILES
    final Config MAIN_CONFIG = PublicTransit.getPlugin().getConfig();

    // General Settings
    // Max distance a player needs to be from a node to interact with it
    DoubleConfigNode PLAYER_DISTANCE_FROM_NODE = new DoubleConfigNode(new ConfigurationNode(
            "node", "distance", "player"), 10, MAIN_CONFIG);

    // Node Default Settings
    DoubleConfigNode DEFAULT_NODE_PRICE = new DoubleConfigNode(new ConfigurationNode(
            "node", "default", "price"), 100, MAIN_CONFIG);
    DoubleConfigNode DEFAULT_NODE_TIME = new DoubleConfigNode(new ConfigurationNode(
            "node", "default", "time"), 15, MAIN_CONFIG);

    // Line Default Settings

    CostTypeConfigNode DEFAULT_LINE_COST_TYPE = new CostTypeConfigNode(new ConfigurationNode(
            "line", "default", "costtype"), CostType.FLAT_RATE, MAIN_CONFIG);

    // Economy Settings

    BooleanConfigNode USE_DEPOSIT_ACCOUNT = new BooleanConfigNode(new ConfigurationNode(
            "economy", "depositaccount", "use"), false, MAIN_CONFIG);
    StringConfigNode DEPOSIT_ACCOUNT = new StringConfigNode(new ConfigurationNode(
            "economy", "depositaccount", "name"), "Government", MAIN_CONFIG);
}

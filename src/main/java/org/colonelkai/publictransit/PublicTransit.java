package org.colonelkai.publictransit;


import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PublicTransit extends JavaPlugin {

    static PublicTransit plugin;
    static Logger logger;

    @Override
    public void onEnable() {
        PublicTransit.plugin = this;
        PublicTransit.logger = this.getLogger();

        this.getLogger().info("Registering commands...");
    }

    @Override
    public void onDisable() {

    }
}

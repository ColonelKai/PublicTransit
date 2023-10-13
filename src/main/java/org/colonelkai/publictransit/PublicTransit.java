package org.colonelkai.publictransit;

import org.colonelkai.publictransit.config.PublicTransitConfig;
import org.core.command.CommandRegister;
import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

public class PublicTransit implements CorePlugin {

    static PublicTransit plugin;
    private final NodeManager nodeManager;
    private final PublicTransitConfig config;
    private Object launcher;
    private Logger logger;

    public PublicTransit() {
        plugin = this;
        this.nodeManager = new NodeManager();
        this.config = new PublicTransitConfig();
    }

    public Logger getLogger() {
        return this.logger;
    }

    public PublicTransitConfig getConfig() {
        return this.config;
    }

    public NodeManager getNodeManager() {
        return this.nodeManager;
    }

    @Override
    public @NotNull String getPluginName() {
        return "PublicTransit";
    }

    @Override
    public @NotNull String getPluginId() {
        return "public_transit";
    }

    @Override
    public @NotNull Object getPlatformLauncher() {
        return this.launcher;
    }

    @Override
    public void onConstruct(@NotNull Object pluginLauncher, @NotNull Logger logger) {
        this.launcher = pluginLauncher;
        this.logger = logger;
    }

    @Override
    public void onRegisterCommands(@NotNull CommandRegister register) {

    }

    @Override
    public @NotNull String getLicence() {
        return "All Rights Reserved";
    }

    @Override
    public @NotNull CorePluginVersion getPluginVersion() {
        return new CorePluginVersion(0, 0, 1);
    }

    @Override
    public void onCoreReady() {
        this.nodeManager.loadAll().forEach(this.nodeManager::register);
        this.logger.log("Loaded lines: " + this.nodeManager.getLines().size());
    }

    public static PublicTransit getPlugin() {
        return plugin;
    }
}

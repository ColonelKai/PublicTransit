package org.colonelkai.publictransit;


import org.colonelkai.publictransit.commands.PublicTransitCommandLauncher;
import org.colonelkai.publictransit.config.PublicTransitConfig;
import org.colonelkai.publictransit.listeners.TravelListener;
import org.core.TranslateCore;
import org.core.command.CommandRegister;
import org.core.logger.Logger;
import org.core.platform.plugin.CorePlugin;
import org.core.platform.plugin.details.CorePluginVersion;
import org.jetbrains.annotations.NotNull;

public class PublicTransit implements CorePlugin {

    private static PublicTransit plugin;
    private final NodeManager nodeManager;
    private final TravelManager travelManager;
    private final PublicTransitConfig config;
    private Object launcher;
    private Logger logger;

    public PublicTransit() {
        plugin = this;
        this.nodeManager = new NodeManager();
        this.travelManager = new TravelManager();
        this.config = new PublicTransitConfig();
    }

    public PublicTransitConfig getConfig() {
        return this.config;
    }

    public Logger getLogger() {
        return this.logger;
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
    public void onCoreReady() {
        TranslateCore.getEventManager().register(this, new TravelListener());
        this.nodeManager.loadAll().forEach(this.nodeManager::register);
        this.logger.log("Loaded lines: " + this.nodeManager.getLines().size());
    }

    @Override
    public @NotNull Object getPlatformLauncher() {
        return this.launcher;
    }

    public TravelManager getTravelManager() {
        return this.travelManager;
    }

    @Override
    public void onConstruct(@NotNull Object pluginLauncher, @NotNull Logger logger) {
        this.launcher = pluginLauncher;
        this.logger = logger;
        this.config.getFile().save();
    }

    @Override
    public void onRegisterCommands(@NotNull CommandRegister register) {
        register.register(new PublicTransitCommandLauncher(PublicTransitCommandLauncher.PUBLIC_TRANSIT));
        register.register(new PublicTransitCommandLauncher(PublicTransitCommandLauncher.TRAVEL));
    }

    @Override
    public @NotNull String getLicence() {
        return "All Rights Reserved";
    }

    @Override
    public @NotNull CorePluginVersion getPluginVersion() {
        return new CorePluginVersion(0, 0, 1);
    }

    public static PublicTransit getPlugin() {
        return plugin;
    }
}

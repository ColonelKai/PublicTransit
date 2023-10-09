package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.node.ConfigNode;
import org.core.TranslateCore;
import org.core.config.ConfigurationStream;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.stream.Stream;

public class PublicTransitConfig implements Config {

    private static final File FILE = new File(PublicTransit.getPlugin().getConfigFolder(),
                                              "config." + TranslateCore.getPlatform().getConfigFormat().getFileType()[0]);

    private ConfigurationStream.ConfigurationFile config;

    public PublicTransitConfig() {
        this.reloadFile();
        this.updateFile();
    }

    @Override
    public ConfigurationStream.ConfigurationFile getFile() {
        return this.config;
    }

    @Override
    public void updateFile() {
        this.getNodes().forEach(node -> {
            try {
                node.currentValue(this.config);
            } catch (Throwable e) {
                this.setDefaultValue(node);
            }
        });
    }

    @Override
    public void reloadFile() {
        this.config = TranslateCore.createConfigurationFile(FILE, TranslateCore.getPlatform().getConfigFormat());
        this.getNodes().forEach(ConfigNode::reset);
    }

    @Override
    public Stream<? extends ConfigNode<?>> getNodes() {
        return Stream
                .of(PublicTransitConfigNodes.class.getDeclaredFields())
                .filter(field -> field.getType().isAssignableFrom(ConfigNode.class))
                .map(field -> {
                    try {
                        return (ConfigNode<?>) field.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }

    public double getPlayerDistanceFromNode() {
        return PublicTransitConfigNodes.PLAYER_DISTANCE_FROM_NODE.currentValue(this.config);
    }

    public void setPlayerDistanceFromNode(double amount) {
        PublicTransitConfigNodes.PLAYER_DISTANCE_FROM_NODE.setValue(this.config, amount);
    }

    private <T> void setDefaultValue(ConfigNode<T> node) {
        node.setValue(this.config, node.defaultValue());
    }
}

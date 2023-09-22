package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.node.ConfigNode;
import org.colonelkai.publictransit.config.node.DoubleConfigNode;
import org.core.TranslateCore;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.stream.Stream;

public class PublicTransitConfig implements Config {


    private static final DoubleConfigNode PLAYER_DISTANCE_FROM_NODE = new DoubleConfigNode(new ConfigurationNode("node", "distance", "player"), 10);

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

    private <T> void setDefaultValue(ConfigNode<T> node){
        node.setValue(this.config, node.defaultValue());
    }

    @Override
    public void reloadFile() {
        this.config = TranslateCore.createConfigurationFile(FILE, TranslateCore.getPlatform().getConfigFormat());
        this.getNodes().forEach(ConfigNode::reset);
    }

    @Override
    public Stream<? extends ConfigNode<?>> getNodes() {
        return Stream
                .of(PublicTransitConfig.class.getDeclaredFields())
                .filter(field -> Modifier.isPrivate(field.getModifiers()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> Modifier.isFinal(field.getModifiers()))
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
        return PLAYER_DISTANCE_FROM_NODE.currentValue(this.config);
    }

    public void setPlayerDistanceFromNode(double amount) {
        PLAYER_DISTANCE_FROM_NODE.setValue(this.config, amount);
    }
}

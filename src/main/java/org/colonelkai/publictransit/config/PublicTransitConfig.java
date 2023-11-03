package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.PublicTransit;
import org.colonelkai.publictransit.config.node.ConfigNode;
import org.core.TranslateCore;
import org.core.config.ConfigurationStream;

import java.io.File;
import java.util.Objects;
import java.util.OptionalInt;
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
                node.get();
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
        return Stream.of(PublicTransitConfigNodes.class.getDeclaredFields()).filter(field -> field.getType().isAssignableFrom(ConfigNode.class)).map(field -> {
            try {
                return (ConfigNode<?>) field.get(null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull);
    }


    private <T> void setDefaultValue(ConfigNode<T> node) {
        node.setValue(this.config, node.defaultValue());
    }

    public OptionalInt getMaximumWeight() {
        if(PublicTransitConfigNodes.WEIGHT_MAXIMUM_DEFAULT.get()==Integer.MAX_VALUE) {
            return OptionalInt.empty();
        } else {
            return OptionalInt.of(PublicTransitConfigNodes.WEIGHT_MAXIMUM_DEFAULT.get());
        }
    }
}

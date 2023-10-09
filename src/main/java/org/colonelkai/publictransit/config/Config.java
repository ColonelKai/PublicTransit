package org.colonelkai.publictransit.config;

import org.colonelkai.publictransit.config.node.ConfigNode;
import org.core.config.ConfigurationStream;

import java.util.stream.Stream;

public interface Config {

    ConfigurationStream.ConfigurationFile getFile();

    void updateFile();

    void reloadFile();

    Stream<? extends ConfigNode<?>> getNodes();


}

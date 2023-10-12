package org.colonelkai.publictransit.config.node.type;

import org.colonelkai.publictransit.config.node.ConfigNodeGetter;
import org.colonelkai.publictransit.config.node.ConfigNodeSetter;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;

import java.util.Optional;

public interface DoubleNodeType extends NodeType {

    ConfigNodeGetter<Double> GETTER = ConfigurationStream::getDouble;

    ConfigNodeSetter<Double> SETTER = ConfigurationStream::set;

}

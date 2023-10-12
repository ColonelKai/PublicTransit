package org.colonelkai.publictransit.config.node.type;

import org.colonelkai.publictransit.config.node.ConfigNodeGetter;
import org.colonelkai.publictransit.config.node.ConfigNodeSetter;
import org.core.config.ConfigurationStream;

public interface IntegerNodeType extends NodeType {

    ConfigNodeGetter<Integer> GETTER = ConfigurationStream::getInteger;

    ConfigNodeSetter<Integer> SETTER = ConfigurationStream::set;

}

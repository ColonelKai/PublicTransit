package org.colonelkai.publictransit.config.node.type;

import org.colonelkai.publictransit.config.node.ConfigNodeGetter;
import org.colonelkai.publictransit.config.node.ConfigNodeSetter;
import org.core.config.ConfigurationStream;

public interface BooleanNodeType extends NodeType {

    ConfigNodeGetter<Boolean> GETTER = ConfigurationStream::getBoolean;

    ConfigNodeSetter<Boolean> SETTER = ConfigurationStream::set;

}

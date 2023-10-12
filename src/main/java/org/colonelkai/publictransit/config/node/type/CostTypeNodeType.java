package org.colonelkai.publictransit.config.node.type;

import org.colonelkai.publictransit.config.node.ConfigNodeGetter;
import org.colonelkai.publictransit.config.node.ConfigNodeSetter;
import org.colonelkai.publictransit.line.CostType;

public interface CostTypeNodeType {

    ConfigNodeSetter<CostType> SETTER = NodeType.enumSetter(CostType.class);
    ConfigNodeGetter<CostType> GETTER = NodeType.enumGetter(CostType.class);

}

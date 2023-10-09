package org.colonelkai.publictransit.utils.serializers;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.easy.config.Serializer;
import org.easy.config.auto.AutoSerializer;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

public class Serializers {

    public static final AutoSerializer<Line> LINE;
    public static final AutoSerializer<Node> NODE;
    public static final PositionSerializer POSITION;
    public static final NodeTypeSerializer NODE_TYPE;
    public static final CostTypeSerializer COST_TYPE;
    public static final LineDirectionSerializer LINE_DIRECTION;
    public static final ComponentSerializer COMPONENT;

    static {
        POSITION = new PositionSerializer();
        NODE_TYPE = new NodeTypeSerializer();
        COST_TYPE = new CostTypeSerializer();
        LINE_DIRECTION = new LineDirectionSerializer();
        COMPONENT = new ComponentSerializer();

        Supplier<Collection<Serializer<?, ?>>> supplier = () -> Arrays.asList(NODE_TYPE, COST_TYPE, POSITION, LINE_DIRECTION, COMPONENT);
        LINE = new AutoSerializer<>(Line.class, supplier);
        NODE = new AutoSerializer<>(Node.class, supplier);
    }

    private Serializers(){
        throw new RuntimeException("Should not create");
    }

}

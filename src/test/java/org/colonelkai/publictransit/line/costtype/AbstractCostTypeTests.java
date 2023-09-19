package org.colonelkai.publictransit.line.costtype;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

class AbstractCostTypeTests {

    public NodeBuilder createTestingNode(Function<NodeBuilder, NodeBuilder> with) {
        ExactPosition position = Mockito.mock(ExactPosition.class);
        Mockito.when(position.toExactPosition()).thenReturn(position);
        return with.apply(new NodeBuilder().setName("Node: " + new Random().nextInt()).setPosition(position));
    }

    public Line createTestingLine(Function<LineBuilder, LineBuilder> with) {
        var line = new LineBuilder().setName("Example Name").setIdentifier("name").setOneWay(true);
        line = with.apply(line);
        return line.build();
    }

    public Line buildLine(Function<LineBuilder, LineBuilder> lineWith, Function<NodeBuilder, NodeBuilder>... nodeWith) {
        List<NodeBuilder> nodes = Stream.of(nodeWith).map(this::createTestingNode).toList();
        return createTestingLine(lineBuilder -> lineWith.apply(lineBuilder.setNodes(nodes)));
    }


}

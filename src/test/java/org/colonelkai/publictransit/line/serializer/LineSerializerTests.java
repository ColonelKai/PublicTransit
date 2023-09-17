package org.colonelkai.publictransit.line.serializer;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.Serializers;
import org.core.world.WorldExtent;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LineSerializerTests {

    @Test
    public void canDeserializeToObject() {
        Map<String, Object> lineNode = new HashMap<>();
        lineNode.put("identifier", "Id");
        lineNode.put("name", "Name");
        lineNode.put("nodes", Collections.emptyList());
        lineNode.put("cost", 1);
        lineNode.put("costType", CostType.FLAT_RATE.name());
        lineNode.put("oneWay", true);
        lineNode.put("oneWayReversed", true);

        //ACT
        Line result;
        try {
            result = Serializers.LINE.deserialize(lineNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //ASSERT
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCost(), 1);
        Assertions.assertEquals(result.getCostType(), CostType.FLAT_RATE);
        Assertions.assertEquals(result.getName(), "Name");
        Assertions.assertTrue(result.isOneWay());
        Assertions.assertTrue(result.isOneWayReversed());
    }

    @Test
    public void canSerializeToMap() {
        var world = Mockito.mock(WorldExtent.class);
        var position = Mockito.mock(SyncExactPosition.class);
        Mockito.when(position.getWorld()).thenReturn(world);


        Node node = new NodeBuilder().setName("example").setType(NodeType.STOP).setPosition(position).build();
        Line line = new LineBuilder()
                .setIdentifier("id")
                .setName("name")
                .setCost(5)
                .setCostType(CostType.FLAT_RATE)
                .setOneWay(true)
                .setOneWayReversed(true)
                .addNodes(node)
                .build();

        //act
        Map<String, Object> asMap = null;
        try {
            asMap = Serializers.LINE.serialize(line);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //assert
        Assertions.assertEquals(asMap.get("identifier"), "id");
        Assertions.assertNotNull(asMap.get("nodes"));
        Assertions.assertInstanceOf(Collection.class, asMap.get("nodes"));
        Assertions.assertEquals(((Collection<?>) asMap.get("nodes")).size(), 1);
        Assertions.assertEquals(asMap.get("oneWayReversed"), true);
        Assertions.assertEquals(asMap.get("oneWay"), true);
        Assertions.assertEquals(asMap.get("costType"), CostType.FLAT_RATE.name());
    }

}

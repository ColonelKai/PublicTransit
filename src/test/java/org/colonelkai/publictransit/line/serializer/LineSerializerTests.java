package org.colonelkai.publictransit.line.serializer;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.colonelkai.publictransit.utils.serializers.Serializers;
import org.core.TranslateCore;
import org.core.platform.PlatformServer;
import org.core.world.WorldExtent;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

public class LineSerializerTests {

    private MockedStatic<TranslateCore> translateCore;

    @BeforeEach
    public void before(){
        translateCore = Mockito.mockStatic(TranslateCore.class);
    }

    @AfterEach
    public void after(){
        translateCore.close();
    }

    @Test
    public void canDeserializeToObject() {
        SyncExactPosition position = Mockito.mock(SyncExactPosition.class);
        PlatformServer server = Mockito.mock(PlatformServer.class);
        WorldExtent world = Mockito.mock(WorldExtent.class);
        translateCore.when(TranslateCore::getServer).thenReturn(server);
        Mockito.when(server.getWorldByPlatformSpecific(Mockito.anyString())).thenReturn(Optional.of(world));
        Mockito.when(world.getPosition(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(position);


        Map<String, Object> positionNode = new HashMap<>();
        positionNode.put("identifier", "worldID");
        positionNode.put("x", 2.0);
        positionNode.put("y", 5.0);
        positionNode.put("z", 6.0);

        Map<String, Object> nodeMap = new HashMap<>();
        nodeMap.put("name", "Name");
        nodeMap.put("location", positionNode);
        nodeMap.put("nodeType", NodeType.STOP.name());

        Map<String, Object> node2Map = new HashMap<>(nodeMap);
        node2Map.replace("name", "Other");

        Map<String, Object> lineNode = new HashMap<>();
        lineNode.put("identifier", "Id");
        lineNode.put("name", "Name");
        lineNode.put("nodes", Arrays.asList(nodeMap, node2Map));
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


        NodeBuilder node = new NodeBuilder().setName("example").setType(NodeType.STOP).setPosition(position);
        NodeBuilder node2 = new NodeBuilder().setName("another").setType(NodeType.STOP).setPosition(position);
        Line line = new LineBuilder()
                .setIdentifier("id")
                .setName("name")
                .setCost(5)
                .setCostType(CostType.FLAT_RATE)
                .setOneWay(true)
                .setOneWayReversed(true)
                .addNodes(node, node2)
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
        Assertions.assertEquals(((Collection<?>) asMap.get("nodes")).size(), 2);
        Assertions.assertEquals(asMap.get("oneWayReversed"), true);
        Assertions.assertEquals(asMap.get("oneWay"), true);
        Assertions.assertEquals(asMap.get("costType"), CostType.FLAT_RATE.name());
    }

}

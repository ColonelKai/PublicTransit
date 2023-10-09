package org.colonelkai.publictransit.node.serializer;

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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NodeSerializerTests {

    private static MockedStatic<TranslateCore> TRANSLATE_CORE;

    @AfterEach
    public void afterEach() {
        TRANSLATE_CORE.close();
    }

    @BeforeEach
    public void beforeEach() {
        TRANSLATE_CORE = Mockito.mockStatic(TranslateCore.class);
    }

    @Test
    public void canDeserializeToObject() {
        SyncExactPosition positionMock = Mockito.mock(SyncExactPosition.class);
        WorldExtent worldMock = Mockito.mock(WorldExtent.class);
        PlatformServer serverMock = Mockito.mock(PlatformServer.class);
        TRANSLATE_CORE.when(TranslateCore::getServer).thenReturn(serverMock);
        Mockito.when(serverMock.getWorldByPlatformSpecific(Mockito.anyString())).thenReturn(Optional.of(worldMock));
        Mockito.when(worldMock.getPosition(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(positionMock);

        Map<String, Object> positionNode = new HashMap<>();
        positionNode.put("identifier", "worldID");
        positionNode.put("x", 2.0);
        positionNode.put("y", 5.0);
        positionNode.put("z", 6.0);

        Map<String, Object> lineNode = new HashMap<>();
        lineNode.put("name", "Name");
        lineNode.put("location", positionNode);
        lineNode.put("nodeType", NodeType.STOP.name());

        //ACT
        Node result;
        try {
            result = Serializers.NODE.deserialize(lineNode);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //ASSERT
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.getName().isPresent());
        Assertions.assertEquals(result.getName().get(), "Name");
    }

    @Test
    public void canSerializeToMap() {
        var world = Mockito.mock(WorldExtent.class);
        ExactPosition position = Mockito.mock(SyncExactPosition.class);
        Mockito.when(position.getWorld()).thenReturn(world);
        Mockito.when(position.toExactPosition()).thenReturn(position);


        Node node = new NodeBuilder().setName("example").setType(NodeType.STOP).setPosition(position).build();

        //act
        Map<String, Object> asMap = null;
        try {
            asMap = Serializers.NODE.serialize(node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //assert
        Assertions.assertEquals(asMap.size(), 4);
        Assertions.assertEquals(asMap.get("name"), "example");
        Assertions.assertNull(asMap.get("time"));
        Assertions.assertEquals(asMap.get("nodeType"), NodeType.STOP.name());
        Object positionObj = asMap.get("location");
        Assertions.assertInstanceOf(Map.class, positionObj);
    }

}

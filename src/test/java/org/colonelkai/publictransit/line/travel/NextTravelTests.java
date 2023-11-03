package org.colonelkai.publictransit.line.travel;

import org.colonelkai.publictransit.fake.position.FakeSyncExactPosition;
import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.colonelkai.publictransit.line.LineDirection;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.node.NodeBuilder;
import org.colonelkai.publictransit.node.NodeType;
import org.core.world.WorldExtent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

public class NextTravelTests {

    @Test
    public void validTravelPositive() {
        WorldExtent world = Mockito.mock(WorldExtent.class);
        Line line = new LineBuilder()
                .setIdentifier("testLine")
                .setDirection(LineDirection.POSITIVE)
                .setCost(0.0)
                .setCostType(CostType.FLAT_RATE)
                .addNodes(new NodeBuilder()
                                  .setName("Start")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 1, 2, 3)), new NodeBuilder()
                                  .setType(NodeType.TRANSITIONAL)
                                  .setPosition(new FakeSyncExactPosition(world, 2, 3, 1)), new NodeBuilder()
                                  .setName("End")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 3, 1, 2)))
                .build();

        Node midNode = line.getNodes().get(1);
        Node endNode = line.getNodes().get(2);

        Travel travel = new TravelBuilder()
                .setTravellingOn(line)
                .setCurrentNode(midNode)
                .setEndingNode(endNode)
                .setOriginalPosition(midNode.getPosition())
                .setPlayerId(UUID.randomUUID())
                .build();

        //act
        Optional<Travel> opTravel = travel.travelToNext();

        //assert
        Assertions.assertTrue(opTravel.isPresent());
        Travel endingTravel = opTravel.get();
        Assertions.assertEquals(endingTravel.getCurrentNode(), endNode);
        Assertions.assertTrue(endingTravel.hasArrived());
    }

    @Test
    public void validTravelNegative() {
        WorldExtent world = Mockito.mock(WorldExtent.class);
        Line line = new LineBuilder()
                .setIdentifier("testLine")
                .setDirection(LineDirection.NEGATIVE)
                .setCost(0.0)
                .setCostType(CostType.FLAT_RATE)
                .addNodes(new NodeBuilder()
                                  .setName("Start")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 1, 2, 3)), new NodeBuilder()
                                  .setType(NodeType.TRANSITIONAL)
                                  .setPosition(new FakeSyncExactPosition(world, 2, 3, 1)), new NodeBuilder()
                                  .setName("End")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 3, 1, 2)))
                .build();

        Node midNode = line.getNodes().get(1);
        Node endNode = line.getNodes().get(0);

        Travel travel = new TravelBuilder()
                .setTravellingOn(line)
                .setCurrentNode(midNode)
                .setEndingNode(endNode)
                .setOriginalPosition(midNode.getPosition())
                .setPlayerId(UUID.randomUUID())
                .build();

        //act
        Optional<Travel> opTravel = travel.travelToNext();

        //assert
        Assertions.assertTrue(opTravel.isPresent());
        Travel endingTravel = opTravel.get();
        Assertions.assertEquals(endingTravel.getCurrentNode(), endNode);
        Assertions.assertTrue(endingTravel.hasArrived());
    }

    @Test
    public void validTravelBackwardOnTwoWay() {
        WorldExtent world = Mockito.mock(WorldExtent.class);
        Line line = new LineBuilder()
                .setIdentifier("testLine")
                .setDirection(LineDirection.POSITIVE)
                .setBiDirectional(true)
                .setCost(0.0)
                .setCostType(CostType.FLAT_RATE)
                .addNodes(new NodeBuilder()
                                  .setName("Start")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 1, 2, 3)), new NodeBuilder()
                                  .setType(NodeType.TRANSITIONAL)
                                  .setPosition(new FakeSyncExactPosition(world, 2, 3, 1)), new NodeBuilder()
                                  .setName("End")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 3, 1, 2)))
                .build();

        Node midNode = line.getNodes().get(1);
        Node endNode = line.getNodes().get(0);

        Travel travel = new TravelBuilder()
                .setTravellingOn(line)
                .setCurrentNode(midNode)
                .setEndingNode(endNode)
                .setTravellingDirection(LineDirection.NEGATIVE)
                .setOriginalPosition(midNode.getPosition())
                .setPlayerId(UUID.randomUUID())
                .build();

        //act
        Optional<Travel> opTravel = travel.travelToNext();

        //assert
        Assertions.assertTrue(opTravel.isPresent());
        Travel endingTravel = opTravel.get();
        Assertions.assertEquals(endingTravel.getCurrentNode(), endNode);
        Assertions.assertTrue(endingTravel.hasArrived());
    }

    @Test
    public void invalidTravelBackwardOnOneWay() {
        WorldExtent world = Mockito.mock(WorldExtent.class);
        Line line = new LineBuilder()
                .setIdentifier("testLine")
                .setDirection(LineDirection.POSITIVE)
                .setBiDirectional(false)
                .setCost(0.0)
                .setCostType(CostType.FLAT_RATE)
                .addNodes(new NodeBuilder()
                                  .setName("Start")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 1, 2, 3)), new NodeBuilder()
                                  .setType(NodeType.TRANSITIONAL)
                                  .setPosition(new FakeSyncExactPosition(world, 2, 3, 1)), new NodeBuilder()
                                  .setName("End")
                                  .setType(NodeType.STOP)
                                  .setPosition(new FakeSyncExactPosition(world, 3, 1, 2)))
                .build();

        Node midNode = line.getNodes().get(1);
        Node endNode = line.getNodes().get(2);

        //act
        //assert
        Assertions.assertThrows(IllegalStateException.class, () -> new TravelBuilder()
                .setTravellingOn(line)
                .setCurrentNode(midNode)
                .setOriginalPosition(midNode.getPosition())
                .setEndingNode(endNode)
                .setTravellingDirection(LineDirection.NEGATIVE)
                .setPlayerId(UUID.randomUUID())
                .build());
    }

}

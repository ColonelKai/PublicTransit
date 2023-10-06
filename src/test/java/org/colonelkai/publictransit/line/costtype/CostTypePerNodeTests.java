package org.colonelkai.publictransit.line.costtype;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.NodeType;
import org.core.world.position.impl.ExactPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CostTypePerNodeTests extends AbstractCostTypeTests {

    private static final CostType TYPE = CostType.PER_NODE;

    @Test
    public void testValidCost() {
        double cost = 2;

        Line line = this.buildLine(builder -> builder.setCost(cost).setCostType(TYPE), builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.STOP).setName("Start").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.TRANSITIONAL).setName("Mid-First").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.TRANSITIONAL).setName("Mid-Second").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.STOP).setName("end").setPosition(position);
        });

        //act
        double amount = line.getPrice();

        //assert
        Assertions.assertEquals(6, amount);
    }

    @Test
    public void testMaxCost() {
        double cost = Double.MAX_VALUE;

        Line line = this.buildLine(builder -> builder.setCost(cost).setCostType(TYPE), builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.STOP).setName("Start").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.TRANSITIONAL).setName("Mid-First").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.TRANSITIONAL).setName("Mid-Second").setPosition(position);
        }, builder -> {
            ExactPosition position = Mockito.mock(SyncExactPosition.class);
            Mockito.when(position.toExactPosition()).thenReturn(position);
            return builder.setType(NodeType.STOP).setName("end").setPosition(position);
        });

        //act
        double amount = line.getPrice();

        //assert
        Assertions.assertEquals(cost * 4, amount);
    }

}

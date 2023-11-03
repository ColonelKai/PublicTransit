package org.colonelkai.publictransit.line.weight;

import org.colonelkai.publictransit.line.CostType;
import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineBuilder;
import org.core.inventory.InventorySnapshot;
import org.core.inventory.item.stack.ItemStackSnapshot;
import org.core.inventory.parts.Slot;
import org.core.inventory.parts.snapshot.SlotSnapshot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeightTests {

    private SlotSnapshot mockSlot(int quantity) {
        ItemStackSnapshot stack = Mockito.mock(ItemStackSnapshot.class);
        Mockito.when(stack.getQuantity()).thenReturn(quantity);
        Mockito.when(stack.createSnapshot()).thenReturn(stack);
        return new SlotSnapshot(null, stack);
    }

    @Test
    public void isZeroWeightValid() {
        InventorySnapshot inventory = Mockito.mock(InventorySnapshot.class);
        Line line = new LineBuilder().setIdentifier("test").setWeight(2).setCost(0).setCostType(CostType.PER_STOP).setBiDirectional(true).build();

        //act
        boolean isValid = line.isValidWeight(inventory);

        //assert
        Assertions.assertTrue(isValid);
    }

    @Test
    public void isSingleWeightValid() {
        InventorySnapshot inventory = Mockito.mock(InventorySnapshot.class);
        Set<Slot> slots = Collections.singleton(this.mockSlot(1));
        Mockito.when(inventory.getSlots()).thenReturn(slots);
        Line line = new LineBuilder().setIdentifier("test").setWeight(2).setCost(0).setCostType(CostType.PER_STOP).setBiDirectional(true).build();

        //act
        boolean isValid = line.isValidWeight(inventory);

        //assert
        Assertions.assertTrue(isValid);
    }

    @Test
    public void isMatchedWeightValid() {
        InventorySnapshot inventory = Mockito.mock(InventorySnapshot.class);
        Set<Slot> slots = Collections.singleton(this.mockSlot(2));
        Mockito.when(inventory.getSlots()).thenReturn(slots);
        Line line = new LineBuilder().setIdentifier("test").setWeight(2).setCost(0).setCostType(CostType.PER_STOP).setBiDirectional(true).build();

        //act
        boolean isValid = line.isValidWeight(inventory);

        //assert
        Assertions.assertTrue(isValid);
    }

    @Test
    public void isOverWeightValid() {
        InventorySnapshot inventory = Mockito.mock(InventorySnapshot.class);
        Set<Slot> slots = IntStream.range(0, 3).mapToObj(t -> this.mockSlot(2)).collect(Collectors.toSet());
        Mockito.when(inventory.getSlots()).thenReturn(slots);
        Line line = new LineBuilder().setIdentifier("test").setWeight(2).setCost(0).setCostType(CostType.PER_STOP).setBiDirectional(true).build();

        //act
        boolean isValid = line.isValidWeight(inventory);

        //assert
        Assertions.assertFalse(isValid);
    }

}

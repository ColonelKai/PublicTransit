package org.colonelkai.publictransit.line.travel;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineDirection;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class TravelBuilder implements Builder<TravelBuilder, Travel> {

    private UUID player;
    private Line travellingOn;
    private Node currentNode;
    private Node endingNode;
    private LineDirection travellingDirection;

    public Line travellingOn() {
        return this.travellingOn;
    }

    public TravelBuilder setTravellingOn(Line travellingOn) {
        this.travellingOn = travellingOn;
        return this;
    }

    public Node currentNode() {
        return this.currentNode;
    }

    public TravelBuilder setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
        return this;
    }

    public Node endingNode() {
        return this.endingNode;
    }

    public TravelBuilder setEndingNode(Node endingNode) {
        this.endingNode = endingNode;
        return this;
    }

    public LineDirection travellingDirection() {
        return this.travellingDirection;
    }

    public TravelBuilder setTravellingDirection(@Nullable LineDirection ridingReversed) {
        this.travellingDirection = ridingReversed;
        return this;
    }

    public UUID player() {
        return this.player;
    }

    public TravelBuilder setPlayer(@NotNull UUID player) {
        this.player = player;
        return this;
    }

    @Override
    public Travel build() {
        return new Travel(this);
    }

    @Override
    public TravelBuilder from(TravelBuilder travelBuilder) {
        this.travellingOn = travelBuilder.travellingOn;
        this.travellingDirection = travelBuilder.travellingDirection;
        this.currentNode = travelBuilder.currentNode;
        this.endingNode = travelBuilder.endingNode;
        return this;
    }
}

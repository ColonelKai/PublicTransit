package org.colonelkai.publictransit.line.travel;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Builder;

public class TravelBuilder implements Builder<TravelBuilder, Travel> {

    private Line travellingOn;
    private Node currentNode;
    private Node endingNode;
    private boolean isRidingReversed;

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

    public boolean isRidingReversed() {
        return this.isRidingReversed;
    }

    public TravelBuilder setRidingReversed(boolean ridingReversed) {
        this.isRidingReversed = ridingReversed;
        return this;
    }

    @Override
    public Travel build() {
        return new Travel(this);
    }

    @Override
    public TravelBuilder from(TravelBuilder travelBuilder) {
        this.travellingOn = travelBuilder.travellingOn;
        this.isRidingReversed = travelBuilder.isRidingReversed;
        this.currentNode = travelBuilder.currentNode;
        this.endingNode = travelBuilder.endingNode;
        return this;
    }
}

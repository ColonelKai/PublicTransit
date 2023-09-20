package org.colonelkai.publictransit.line.travel;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineDirection;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Buildable;

import java.util.Objects;
import java.util.Optional;

public class Travel implements Buildable<TravelBuilder, Travel> {

    private final Line travellingOn;
    private final Node currentNode;
    private final Node endingNode;
    private final boolean isRidingReversed;

    Travel(TravelBuilder builder) {
        this.currentNode = Objects.requireNonNull(builder.currentNode());
        this.endingNode = Objects.requireNonNull(builder.endingNode());
        this.travellingOn = Objects.requireNonNull(builder.travellingOn());
        this.isRidingReversed = builder.isRidingReversed();

        if (!this.travellingOn.getNodes().contains(this.currentNode)) {
            throw new IllegalStateException("Travelling On must contain the current node");
        }
        if (!this.travellingOn.getNodes().contains(this.endingNode)) {
            throw new IllegalStateException("Travelling On must contain the ending node");
        }
        if (!this.travellingOn.isBiDirectional() && (this.travellingOn.getDirection().isPositive() == this.isRidingReversed)) {
            throw new IllegalStateException("Cannot ride backwards on a one way Line");
        }
    }

    public boolean hasArrived() {
        return this.currentNode.equals(this.endingNode);
    }

    @Override
    public TravelBuilder toBuilder() {
        return new TravelBuilder()
                .setTravellingOn(this.travellingOn)
                .setEndingNode(this.endingNode)
                .setCurrentNode(this.currentNode)
                .setRidingReversed(this.isRidingReversed);
    }

    public Optional<Travel> travelToNext() {
        if (this.hasArrived()) {
            return Optional.empty();
        }
        int index = this.travellingOn.getNodes().indexOf(this.currentNode);
        LineDirection direction = this.travellingOn.getDirection();
        boolean nextIsReversed = this.isRidingReversed;
        if (this.travellingOn.isBiDirectional() && direction.isAtEndOfLine(index, this.isRidingReversed, this.travellingOn.getNodes())) {
            nextIsReversed = !this.isRidingReversed;
        }
        Optional<Node> opNext = direction.getNextNode(index, nextIsReversed, this.travellingOn.getNodes());
        if (opNext.isPresent()) {
            final boolean finalNextIsReversed = nextIsReversed;
            return opNext.map(node -> this.toBuilder().setCurrentNode(node).setRidingReversed(finalNextIsReversed).build());
        }
        return Optional.empty();
    }

    public Line getTravellingOn() {
        return this.travellingOn;
    }

    public Node getCurrentNode() {
        return this.currentNode;
    }

    public Node getEndingNode() {
        return this.endingNode;
    }

    public boolean isRidingReversed() {
        return this.isRidingReversed;
    }
}

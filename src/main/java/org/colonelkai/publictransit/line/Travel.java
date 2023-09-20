package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;

import java.util.Optional;

public class Travel {

    private final Line travellingOn;
    private final Node currentDestination;
    private final boolean isRidingReversed;

    public Travel(Line line, Node node, boolean isRidingReversed) {
        this.currentDestination = node;
        this.travellingOn = line;
        this.isRidingReversed = isRidingReversed;
    }

    public Optional<Travel> travelToNext() {
        int index = this.travellingOn.getNodes().indexOf(this.currentDestination);
        LineDirection direction = this.travellingOn.getDirection();
        boolean nextIsReversed = this.isRidingReversed;
        if (this.travellingOn.isBiDirectional() && direction.isAtEndOfLine(index, this.isRidingReversed, this.travellingOn.getNodes())) {
            nextIsReversed = !this.isRidingReversed;
        }
        Optional<Node> opNext = direction.getNextNode(index, nextIsReversed, this.travellingOn.getNodes());
        if (opNext.isPresent()) {
            final boolean finalNextIsReversed = nextIsReversed;
            return opNext.map(node -> new Travel(this.travellingOn, node, finalNextIsReversed));
        }
        return Optional.empty();
    }

    public Line getTravellingOn() {
        return this.travellingOn;
    }

    public Node getCurrentDestination() {
        return this.currentDestination;
    }

    public boolean isRidingReversed() {
        return this.isRidingReversed;
    }
}

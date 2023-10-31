package org.colonelkai.publictransit.line.travel;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.line.LineDirection;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.Buildable;
import org.core.entity.living.human.player.LivePlayer;
import org.core.entity.living.human.player.Player;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Travel implements Buildable<TravelBuilder, Travel> {

    private UUID player;
    private final Line travellingOn;
    private final Node currentNode;
    private final Node endingNode;
    private final Node startingNode;
    private final LineDirection travellingDirection;

    Travel(TravelBuilder builder) {
        this.currentNode = Objects.requireNonNull(builder.currentNode());
        this.startingNode = Objects.requireNonNull(builder.currentNode());
        this.endingNode = Objects.requireNonNull(builder.endingNode());
        this.travellingOn = Objects.requireNonNull(builder.travellingOn());
        this.travellingDirection = Objects.requireNonNullElse(builder.travellingDirection(), this.travellingOn.getDirection());

        if (!this.travellingOn.getNodes().contains(this.currentNode)) {
            throw new IllegalStateException("Travelling On must contain the current node");
        }
        if (!this.travellingOn.getNodes().contains(this.endingNode)) {
            throw new IllegalStateException("Travelling On must contain the ending node");
        }
        if (!this.travellingOn.isBiDirectional() && (this.travellingOn.getDirection() != this.travellingDirection)) {
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
                .setTravellingDirection(this.travellingDirection);
    }

    public Optional<Travel> travelToNext() {
        if (this.hasArrived()) {
            return Optional.empty();
        }
        int index = this.travellingOn.getNodes().indexOf(this.currentNode);
        LineDirection nextIsReversed = this.travellingDirection;
        if (this.travellingOn.isBiDirectional() && nextIsReversed.isAtEndOfLine(index, this.travellingOn.getNodes())) {
            nextIsReversed = nextIsReversed.getOpposite();
        }
        Optional<Node> opNext = nextIsReversed.getNextNode(index, this.travellingOn.getNodes());
        if (opNext.isPresent()) {
            final LineDirection finalNextIsReversed = nextIsReversed;
            return opNext.map(node -> this.toBuilder().setCurrentNode(node).setTravellingDirection(finalNextIsReversed).build());
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

    public Node getStartingNode() {
        return this.startingNode;
    }

    public UUID getPlayer() {
        return this.player;
    }

    public LineDirection getTravellingDirection() {
        return this.travellingDirection;
    }
}

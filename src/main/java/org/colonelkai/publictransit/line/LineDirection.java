package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;

import java.util.List;
import java.util.Optional;

public enum LineDirection {

    //one way normal
    POSITIVE_SINGLE(false, 1),
    //two way normal
    POSITIVE_FLIP(true, 1),
    //one way reversed
    NEGATIVE_SINGLE(false, -1);

    private final int addition;
    private final boolean canFlipBack;

    LineDirection(boolean canFlipBack, int addition) {
        this.addition = addition;
        this.canFlipBack = canFlipBack;
    }

    public Optional<Node> getNextNode(int currentIndex, List<Node> nodes) {
        if (nodes.isEmpty()) {
            return Optional.empty();
        }
        int index = currentIndex + this.addition;
        if (index == -1 && this.canFlipBack) {
            //gets first
            return Optional.of(nodes.get(0));
        }
        if (index == nodes.size() && this.canFlipBack) {
            //gets last
            return Optional.of(nodes.get(nodes.size() - 1));
        }
        if (index < nodes.size()) {
            //valid index
            return Optional.of(nodes.get(index));
        }
        return Optional.empty();
    }

}

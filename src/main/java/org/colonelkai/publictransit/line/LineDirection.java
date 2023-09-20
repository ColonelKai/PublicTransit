package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.node.Node;

import java.util.List;
import java.util.Optional;

public enum LineDirection {

    POSITIVE(true),
    NEGATIVE(false);

    private final boolean isPositive;

    LineDirection(boolean isPositive) {
        this.isPositive = isPositive;
    }

    public boolean isPositive() {
        return this.isPositive;
    }

    private int getAddition(boolean isReverse) {
        int addition = this.isPositive ? 1 : -1;
        if (isReverse) {
            return -addition;
        }
        return addition;
    }

    public Optional<Node> getNextNode(int currentIndex, boolean isReverse, List<Node> nodes) {
        if (nodes.isEmpty()) {
            return Optional.empty();
        }
        return this.next(currentIndex, isReverse, nodes);
    }

    private Optional<Node> next(int currentIndex, boolean isReverse, List<Node> nodes) {
        int index = currentIndex + this.getAddition(isReverse);
        if (index < nodes.size()) {
            //valid index
            return Optional.of(nodes.get(index));
        }
        return Optional.empty();
    }

    public boolean isAtEndOfLine(int currentIndex, boolean isReverse, List<Node> nodes) {
        return this.next(currentIndex, isReverse, nodes).isEmpty();
    }

}

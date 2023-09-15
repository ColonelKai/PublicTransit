package org.colonelkai.publictransit.line;

import org.colonelkai.publictransit.nodes.Node;
import org.colonelkai.publictransit.nodes.NodeTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Line {

    final String identifier;
    String name;
    List<Node> nodes;

    int cost;
    CostType costType;

    boolean oneWay;
    boolean oneWayReversed;

    public Line(String identifier, String name, int cost, CostType costType, boolean oneWay, boolean oneWayReversed) {
        this.identifier = identifier;
        this.name = name;
        this.cost = cost;
        this.costType = costType;
        this.oneWay = oneWay;
        this.oneWayReversed = oneWayReversed;
    }

    // get every node between two given integers
    // returns map because we gotta preserve their index, as that's how we identify them
    public Map<Integer, Node> getAllNodesBetween(int start, int end) {
        Map<Integer, Node> map = new HashMap<>();
        int cursor = start;

        do {
            map.put(cursor, this.nodes.get(cursor));
            if(start<end) cursor++;
            else if(start>end) cursor--;
        } while (cursor != end);

        return map;
    }

    public int getPrice(int start, int end) {
        switch (this.costType) {
            case FLAT_RATE -> {
                return this.cost;
            }
            case PER_NODE -> {
                return Math.abs(start - end);
            }
            case PER_STOP -> {
                Map<Integer, Node> nodes = getAllNodesBetween(start, end);
                return
                        (nodes.entrySet().parallelStream()
                                .filter(
                                        integerNodeEntry -> integerNodeEntry.getValue().getNodeType() == NodeTypes.STOP)
                                .collect(Collectors.toSet())).size();
            }
        }
        return 0;
    }

    // GETTER SETTERS

    public boolean addNode(Node node) {
        return this.nodes.add(node);
    }

    public void addNode(Node node, int index) {
        this.nodes.add(index, node);
    }

    public boolean removeNode(Node node) {
        return this.nodes.remove(node);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setCostType(CostType costType) {
        this.costType = costType;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public void setOneWayReversed(boolean oneWayReversed) {
        this.oneWayReversed = oneWayReversed;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getCost() {
        return cost;
    }

    public CostType getCostType() {
        return costType;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public boolean isOneWayReversed() {
        return oneWayReversed;
    }
}

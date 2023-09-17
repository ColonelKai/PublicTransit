package org.colonelkai.publictransit;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeManager {

    private final Collection<Line> lines = new LinkedTransferQueue<>();

    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    public Stream<Node> getNodes() {
        return this.lines.stream().flatMap(node -> node.getNodes().stream()).distinct();
    }

    public void register(@NotNull Line line) {
        this.lines.add(line);
    }

    public void update(@NotNull Node node) {
        var lines = this.lines.stream().filter(line -> line.getNodes().stream().anyMatch(lineNode -> lineNode.getName().equals(node.getName()))).toList();

        var add = lines.stream().map(line -> line.toBuilder().removeNode(node.getName()).build()).collect(Collectors.toList());

        this.lines.removeAll(lines);
        this.lines.addAll(add);
    }

}

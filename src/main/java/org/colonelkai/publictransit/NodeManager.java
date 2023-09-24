package org.colonelkai.publictransit;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.serializers.Serializers;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.core.world.position.Positionable;
import org.core.world.position.impl.Position;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeManager {

    public static final File LINES_DATA_PATH = new File(PublicTransit.getPlugin().getConfigFolder(), "data/lines");
    private final Collection<Line> lines = new LinkedTransferQueue<>();

    public Optional<Node> getClosestNode(Positionable<?> positionable) {
        return this.getClosestNode(positionable.getPosition());
    }

    public Optional<Node> getClosestNode(Position<?> position) {
        return this
                .getNodes()
                .filter(node -> node.getPosition().getWorld().equals(position.getWorld()))
                .min(Comparator.comparing(node -> node.getPosition().getPosition().distanceSquared(position.getPosition())));
    }

    private ConfigurationFormat getFormat(String name) {
        return this
                .getFormats()
                .filter(format -> Arrays.stream(format.getFileType()).anyMatch(t -> name.toLowerCase().endsWith(t.toLowerCase())))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No supported format for '" + name + "'"));
    }

    private Stream<ConfigurationFormat> getFormats() {
        return Arrays
                .stream(ConfigurationFormat.class.getDeclaredFields())
                .filter(field -> Modifier.isFinal(field.getModifiers()))
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .filter(field -> Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType().isAssignableFrom(ConfigurationFormat.class))
                .map(field -> {
                    try {
                        return (ConfigurationFormat) field.get(null);
                    } catch (Throwable e) {
                        //should never hit
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }

    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    public Stream<Line> getLinesFor(Node node) {
        return this.lines.stream().filter(line -> line.getNodes().contains(node));
    }

    public Stream<Line> getLinesFor(Node... nodes) {
        return this.lines.stream().filter(line -> Stream.of(nodes).allMatch(node -> line.getNodes().contains(node)));
    }

    public Stream<Node> getNodes() {
        return this.lines.stream().flatMap(node -> node.getNodes().stream()).distinct();
    }

    public Line load(File file) throws Exception {
        ConfigurationFormat format = this.getFormat(file.getName());
        ConfigurationStream.ConfigurationFile config = TranslateCore.createConfigurationFile(file, format);
        Map<String, Object> map = config
                .getMap(new ConfigurationNode())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
        return Serializers.LINE.deserialize(map);
    }

    public Stream<Line> loadAll() {
        return this.loadAll(LINES_DATA_PATH);
    }

    public Stream<Line> loadAll(File folder) {
        File[] files = folder.listFiles();
        if (null == files) {
            return Stream.empty();
        }
        return Arrays.stream(files).map(file -> {
            try {
                return this.load(file);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull);
    }

    public void register(@NotNull Line line) {
        if (this.lines.stream().anyMatch(l -> l.getIdentifier().equals(line.getIdentifier()))) {
            throw new IllegalArgumentException("Line of '" + line.getName() + "' is already registered");
        }
        this.lines.add(line);
    }

    public void save(Line line, File to) throws Exception {
        ConfigurationFormat format = TranslateCore.getPlatform().getConfigFormat();
        File file = new File(to, format.getFileType()[0]);
        ConfigurationStream.ConfigurationFile config = TranslateCore.createConfigurationFile(file, format);

        Map<String, Object> map = Serializers.LINE.serialize(line);
        config.set(new ConfigurationNode(), map);
        config.save();
    }

    public void update(@NotNull Line line) {
        List<Line> toRemove = this.lines.stream().filter(l -> l.getName().equals(line.getName())).toList();
        this.lines.removeAll(toRemove);
        this.lines.add(line);
    }

}

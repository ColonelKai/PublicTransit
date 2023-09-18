package org.colonelkai.publictransit;

import org.colonelkai.publictransit.line.Line;
import org.colonelkai.publictransit.node.Node;
import org.colonelkai.publictransit.utils.serializers.Serializers;
import org.core.TranslateCore;
import org.core.config.ConfigurationFormat;
import org.core.config.ConfigurationNode;
import org.core.config.ConfigurationStream;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.LinkedTransferQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NodeManager {

    private final Collection<Line> lines = new LinkedTransferQueue<>();

    private static final File LINES_DATA_PATH = new File(PublicTransit.getPlugin().getConfigFolder(),"data/lines");

    public Collection<Line> getLines() {
        return Collections.unmodifiableCollection(this.lines);
    }

    public Stream<Node> getNodes() {
        return this.lines.stream().flatMap(node -> node.getNodes().stream()).distinct();
    }

    public void register(@NotNull Line line) {
        if(this.lines.stream().anyMatch(l -> l.getName().equals(line.getName()))){
            throw new IllegalArgumentException("Line of '" + line.getName() + "' is already registered");
        }
        this.lines.add(line);
    }

    public Stream<Line> loadAll(){
        return loadAll(LINES_DATA_PATH);
    }

    public Stream<Line> loadAll(File folder){
        File[] files = folder.listFiles();
        if(null == files){
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

    public void save(Line line, File to) throws Exception {
        ConfigurationFormat format = TranslateCore.getPlatform().getConfigFormat();
        File file = new File(to, format.getFileType()[0]);
        ConfigurationStream.ConfigurationFile config = TranslateCore.createConfigurationFile(file, format);

        Map<String, Object> map = Serializers.LINE.serialize(line);
        config.set(new ConfigurationNode(), map);
        config.save();
    }

    private ConfigurationFormat getFormat(String name){
        return getFormats().filter(format -> Arrays.stream(format.getFileType()).anyMatch(t -> name.toLowerCase().endsWith(t.toLowerCase()))).findAny().orElseThrow(() -> new IllegalArgumentException("No supported format for '" + name + "'"));
    }

    private Stream<ConfigurationFormat> getFormats(){
        return Arrays.stream(ConfigurationFormat.class.getDeclaredFields()).filter(field -> Modifier.isFinal(field.getModifiers())).filter(field -> Modifier.isPublic(field.getModifiers())).filter(field -> Modifier.isStatic(field.getModifiers())).filter(field -> field.getType().isAssignableFrom(ConfigurationFormat.class)).map(field -> {
            try{
                return (ConfigurationFormat)field.get(null);
            }catch (Throwable e){
                //should never hit
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull);
    }

    public void update(@NotNull Node node) {
        var lines = this.lines.stream().filter(line -> line.getNodes().stream().anyMatch(lineNode -> lineNode.getName().equals(node.getName()))).toList();
        var add = lines.stream().map(line -> line.toBuilder().removeNode(node.getName()).build()).toList();
        this.lines.removeAll(lines);
        this.lines.addAll(add);
    }

    public void update(@NotNull Line line){
        List<Line> toRemove = this.lines.stream().filter(l -> l.getName().equals(line.getName())).toList();
        this.lines.removeAll(toRemove);
        this.lines.add(line);
    }

}

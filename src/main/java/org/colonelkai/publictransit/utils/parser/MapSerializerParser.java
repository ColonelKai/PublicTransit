package org.colonelkai.publictransit.utils.parser;

import org.core.config.parser.Parser;
import org.easy.config.Serializer;

import java.util.Map;
import java.util.Optional;

class MapSerializerParser<T> implements Parser<Map<String, Object>, T> {

    private final Serializer.KeyValue<T> serializer;

    public MapSerializerParser(Serializer.KeyValue<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    public Optional<T> parse(Map<String, Object> original) {
        try {
            return Optional.of(this.serializer.deserialize(original));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Object> unparse(T value) {
        try {
            return this.serializer.serialize(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package org.colonelkai.publictransit.utils.parser;

import org.core.config.parser.Parser;
import org.core.config.parser.StringParser;
import org.easy.config.Serializer;

import java.util.Map;
import java.util.Optional;

class StringSerializerParser<T> implements StringParser<T> {

    private final Serializer.Text<T> serializer;

    public StringSerializerParser(Serializer.Text<T> serializer) {
        this.serializer = serializer;
    }

    @Override
    public Optional<T> parse(String original) {
        try {
            return Optional.of(this.serializer.deserialize(original));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public String unparse(T value) {
        try {
            return this.serializer.serialize(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package org.colonelkai.publictransit.utils.serializers;

import org.colonelkai.publictransit.line.LineDirection;
import org.easy.config.Serializer;

public class LineDirectionSerializer implements Serializer.Text<LineDirection> {
    @Override
    public String serialize(LineDirection value) {
        return value.name();
    }

    @Override
    public LineDirection deserialize(String type) throws IllegalArgumentException {
        return LineDirection.valueOf(type.toUpperCase());
    }

    @Override
    public Class<?> ofType() {
        return LineDirection.class;
    }
}

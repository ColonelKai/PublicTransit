package org.colonelkai.publictransit.utils.serializers;

import org.colonelkai.publictransit.line.CostType;
import org.easy.config.Serializer;

public class CostTypeSerializer implements Serializer.Text<CostType> {
    @Override
    public String serialize(CostType value) {
        return value.name();
    }

    @Override
    public CostType deserialize(String type) throws IllegalArgumentException {
        return CostType.valueOf(type.toUpperCase());
    }

    @Override
    public Class<?> ofType() {
        return CostType.class;
    }
}

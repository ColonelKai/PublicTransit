package org.colonelkai.publictransit.utils.serializers;

import net.kyori.adventure.text.Component;
import org.core.utils.ComponentUtils;
import org.easy.config.Serializer;

public class ComponentSerializer implements Serializer.Text<Component> {
    @Override
    public String serialize(Component value) throws Exception {
        return ComponentUtils.toGson(value);
    }

    @Override
    public Component deserialize(String type) throws Exception {
        return ComponentUtils.fromGson(type);
    }

    @Override
    public Class<?> ofType() {
        return Component.class;
    }
}

package org.colonelkai.publictransit.utils.serializers;

import org.core.TranslateCore;
import org.core.world.WorldExtent;
import org.core.world.position.impl.Position;
import org.easy.config.Serializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PositionSerializer implements Serializer.KeyValue<Position<?>> {

    private static final String ID = "identifier";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";

    @Override
    public Map<String, Object> serialize(Position<?> value) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, value.getWorld().getPlatformUniqueId());
        map.put(X, value.getX());
        map.put(Y, value.getY());
        map.put(Z, value.getZ());
        return map;
    }

    @Override
    public Position<?> deserialize(Map<String, Object> type) throws Exception {
        Object worldId = type.get(ID);
        if (worldId == null) {
            throw new Exception("World id missing");
        }
        Object x = type.get(X);
        Object y = type.get(Y);
        Object z = type.get(Z);
        if(x == null){
            throw new Exception("Missing X");
        }
        if(y == null){
            throw new Exception("Missing Y");
        }
        if(z == null){
            throw new Exception("Missing Z");
        }

        Optional<WorldExtent> opWorld = TranslateCore.getServer().getWorldByPlatformSpecific((String)worldId);
        if(opWorld.isEmpty()){
            throw new Exception("No world by the id of " + worldId);
        }
        if((x instanceof Double) || (y instanceof Double) || (z instanceof Double)){
            return opWorld.get().getPosition((double)x, (double)y, (double)z);
        }

        return opWorld.get().getPosition((int)x, (int)y, (int)z);
    }

    @Override
    public Class<?> ofType() {
        return Position.class;
    }
}

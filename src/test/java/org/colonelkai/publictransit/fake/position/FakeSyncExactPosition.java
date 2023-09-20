package org.colonelkai.publictransit.fake.position;

import org.core.entity.EntitySnapshot;
import org.core.entity.EntityType;
import org.core.entity.LiveEntity;
import org.core.entity.living.human.player.LivePlayer;
import org.core.platform.plugin.Plugin;
import org.core.threadsafe.FutureResult;
import org.core.vector.type.Vector3;
import org.core.world.WorldExtent;
import org.core.world.position.block.details.BlockDetails;
import org.core.world.position.block.details.BlockSnapshot;
import org.core.world.position.block.entity.LiveTileEntity;
import org.core.world.position.flags.PositionFlag;
import org.core.world.position.impl.async.ASyncBlockPosition;
import org.core.world.position.impl.async.ASyncExactPosition;
import org.core.world.position.impl.sync.SyncBlockPosition;
import org.core.world.position.impl.sync.SyncExactPosition;
import org.core.world.position.impl.sync.SyncPosition;

import java.util.Optional;

public class FakeSyncExactPosition implements SyncExactPosition {

    private final WorldExtent world;
    private final Vector3<Double> position;

    public FakeSyncExactPosition(WorldExtent world, double x, double y, double z) {
        this(world, Vector3.valueOf(x, y, z));
    }

    public FakeSyncExactPosition(WorldExtent world, Vector3<Double> position) {
        this.world = world;
        this.position = position;
    }

    @Override
    public BlockSnapshot.SyncBlockSnapshot getBlockDetails() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SyncPosition<Double> setBlock(BlockDetails details, PositionFlag.SetFlag... flags) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SyncPosition<Double> setBlock(BlockDetails details, LivePlayer... player) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SyncPosition<Double> resetBlock(LivePlayer... player) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Optional<LiveTileEntity> getTileEntity() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public <E extends LiveEntity, S extends EntitySnapshot<E>> Optional<S> createEntity(EntityType<E, ? extends S> type) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public SyncPosition<Double> destroy() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Vector3<Integer> getChunkPosition() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public WorldExtent getWorld() {
        return this.world;
    }

    @Override
    public Vector3<Double> getPosition() {
        return this.position;
    }

    @Override
    public SyncBlockPosition toBlockPosition() {
        throw new RuntimeException("Not implemented");
    }

}

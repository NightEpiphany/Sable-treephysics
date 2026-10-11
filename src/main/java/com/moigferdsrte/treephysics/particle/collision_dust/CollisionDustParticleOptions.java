package com.moigferdsrte.treephysics.particle.collision_dust;

import com.moigferdsrte.treephysics.index.TreePhysicsParticleTypes;
import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record CollisionDustParticleOptions(BlockState state) implements ParticleOptions {
    public static final MapCodec<CollisionDustParticleOptions> CODEC = BlockState.CODEC
            .fieldOf("state").xmap(CollisionDustParticleOptions::new, CollisionDustParticleOptions::state);

    public static final StreamCodec<ByteBuf, CollisionDustParticleOptions> STREAM_CODEC =
            ByteBufCodecs.INT.map(CollisionDustParticleOptions::new, CollisionDustParticleOptions::getId);

    public CollisionDustParticleOptions(int stateId) {
        this(Block.BLOCK_STATE_REGISTRY.byId(stateId));
    }

    public int getId() {
        return Block.BLOCK_STATE_REGISTRY.getId(this.state);
    }

    @Override
    public ParticleType<?> getType() {
        return TreePhysicsParticleTypes.COLLISION_DUST.value();
    }
}

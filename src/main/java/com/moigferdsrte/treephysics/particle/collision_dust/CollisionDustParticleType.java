package com.moigferdsrte.treephysics.particle.collision_dust;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CollisionDustParticleType extends ParticleType<CollisionDustParticleOptions> {
    public CollisionDustParticleType(boolean overrideLimitter) {
        super(overrideLimitter);
    }

    @Override
    public MapCodec<CollisionDustParticleOptions> codec() {
        return CollisionDustParticleOptions.CODEC;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CollisionDustParticleOptions> streamCodec() {
        return CollisionDustParticleOptions.STREAM_CODEC;
    }
}

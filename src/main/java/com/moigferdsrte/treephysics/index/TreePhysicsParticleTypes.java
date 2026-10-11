package com.moigferdsrte.treephysics.index;

import com.moigferdsrte.treephysics.TreePhysics;
import com.moigferdsrte.treephysics.particle.collision_dust.CollisionDustParticleType;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;

public class TreePhysicsParticleTypes {
    public static final Holder.Reference<CollisionDustParticleType> COLLISION_DUST = Registry.registerForHolder(
            BuiltInRegistries.PARTICLE_TYPE,
            TreePhysics.path("collision_dust"),
            new CollisionDustParticleType(false)
    );

    public static void init() {

    }

}

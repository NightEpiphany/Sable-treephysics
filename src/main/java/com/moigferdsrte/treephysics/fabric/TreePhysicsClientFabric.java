package com.moigferdsrte.treephysics.fabric;

import com.moigferdsrte.treephysics.TreePhysics;
import com.moigferdsrte.treephysics.TreePhysicsClient;
import com.moigferdsrte.treephysics.index.TreePhysicsClientConfig;
import com.moigferdsrte.treephysics.index.TreePhysicsParticleTypes;
import com.moigferdsrte.treephysics.networking.UpdateClientTrees;
import com.moigferdsrte.treephysics.particle.collision_dust.CollisionDustProvider;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import fuzs.forgeconfigapiport.fabric.api.v5.client.ConfigScreenFactoryRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;

public class TreePhysicsClientFabric {
    public static void init() {
        TreePhysicsClient.init();
        ConfigRegistry.INSTANCE.register(TreePhysics.MOD_ID, ModConfig.Type.CLIENT, TreePhysicsClientConfig.SPEC);
        ConfigScreenFactoryRegistry.INSTANCE.register(TreePhysics.MOD_ID, (title, parent) -> new ConfigurationScreen(TreePhysics.MOD_ID, parent));

        ClientPlayNetworking.registerGlobalReceiver(UpdateClientTrees.TYPE, (payload, context) ->
                context.client().execute(payload::applyClient));
        ParticleProviderRegistry.getInstance().register(TreePhysicsParticleTypes.COLLISION_DUST.value(), CollisionDustProvider::new);
    }
}

package com.moigferdsrte.treephysics.fabric;

import com.moigferdsrte.treephysics.TreePhysics;
import com.moigferdsrte.treephysics.api.grouping.BlockGroupingManager;
import com.moigferdsrte.treephysics.event.CommonEvents;
import com.moigferdsrte.treephysics.index.TreePhysicsCommands;
import com.moigferdsrte.treephysics.index.TreePhysicsConfig;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.config.ModConfig;

import static com.moigferdsrte.treephysics.TreePhysics.path;

public class TreePhysicsFabric {
    public static void init() {
        TreePhysics.init();
        ConfigRegistry.INSTANCE.register(TreePhysics.MOD_ID, ModConfig.Type.COMMON, TreePhysicsConfig.SPEC);
        ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(path("grouping"), BlockGroupingManager.INSTANCE);
        registerEventListeners();
    }

    private static void registerEventListeners() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            CommonEvents.playerBreakBlock(world, player, pos);
            return true;
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            CommonEvents.playerLoggedIn(handler.player);
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            TreePhysicsCommands.registerCommands(registryAccess, dispatcher);
        });

    }
}

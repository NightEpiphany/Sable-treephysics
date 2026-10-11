package com.moigferdsrte.treephysics.networking;

import com.moigferdsrte.treephysics.TreePhysics;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class TreePhysicsPackets {
    public static void init() {
        PayloadTypeRegistry.clientboundPlay().register(UpdateClientTrees.TYPE, UpdateClientTrees.CODEC);
    }
}

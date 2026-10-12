package com.moigferdsrte.treephysics;

import com.moigferdsrte.treephysics.api.manager.ClientTreeManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public final class TreePhysicsClient {

    public static final ClientTreeManager TREE_MANAGER = new ClientTreeManager();

    public static void init() {
    }

}

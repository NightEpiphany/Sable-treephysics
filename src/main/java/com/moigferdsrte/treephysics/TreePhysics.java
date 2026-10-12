package com.moigferdsrte.treephysics;

import com.moigferdsrte.treephysics.event.CommonEvents;
import com.moigferdsrte.treephysics.index.TreePhysicsParticleTypes;
import com.moigferdsrte.treephysics.networking.TreePhysicsPackets;
import com.mojang.logging.LogUtils;
import dev.ryanhcode.sable.platform.SableEventPlatform;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

public final class TreePhysics {
    public static final String MOD_ID = "treephysics";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        SableEventPlatform.INSTANCE.onSubLevelContainerReady(CommonEvents::containerReady);
        SableEventPlatform.INSTANCE.onPostPhysicsTick(CommonEvents::postPhysicsTick);

        TreePhysicsPackets.init();
        TreePhysicsParticleTypes.init();
    }

    public static Identifier path(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }

}

package com.moigferdsrte.treephysics;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public final class TreePhysicsMixinPlugin implements IMixinConfigPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.startsWith("com.moigferdsrte.treephysics.mixin.compatibility.")) {
            final String[] parts = mixinClassName.split("\\.");
            if (parts.length < 5) {
                return true;
            }

            final String modId = parts[3].equals("mixin") ? parts[5] : parts[6];
            return FabricLoader.getInstance().isModLoaded(modId);
        }

        return true;
    }

}

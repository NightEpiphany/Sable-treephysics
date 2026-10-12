package com.moigferdsrte.treephysics.mixin.compatibility.sodium;

import net.minecraft.client.Camera;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "dev.ryanhcode.sable.sublevel.render.vanilla.VanillaChunkedSubLevelRenderData")
public abstract class VanillaChunkedSubLevelRenderDataMixin {

    @Inject(method = "compileSections", at = @At("HEAD"), cancellable = true)
    private void treephysics$skipVanillaSectionCompilationWithSodium(
            final PrioritizeChunkUpdates chunkUpdates,
            final RenderRegionCache renderRegionCache,
            final Camera camera,
            final CallbackInfo ci
    ) {
        ci.cancel();
    }
}

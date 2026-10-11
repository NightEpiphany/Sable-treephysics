package com.moigferdsrte.treephysics.mixin.rooted_dirt_placement;

import com.moigferdsrte.treephysics.api.util.TreeUtil;
import com.moigferdsrte.treephysics.index.TreePhysicsConfig;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.BiConsumer;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin {

    @WrapMethod(method = "placeBelowTrunkBlock")
    private static void treephysics$setDirtAt(WorldGenLevel level, BiConsumer<BlockPos, BlockState> trunkSetter, RandomSource random, BlockPos pos, TreeFeature tree, Operation<Void> original) {
        if(TreePhysicsConfig.ROOTED_DIRT_GENERATION.getAsBoolean()) {
            trunkSetter.accept(pos, TreeUtil.getDefaultRoot());
            return;
        }

        original.call(level, trunkSetter, random, pos, tree);
    }

}

package com.moigferdsrte.treephysics.api.flood_fill;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface Rule {
    boolean canSpread(BlockPos fromPos, BlockPos toPos, BlockState fromState, BlockState toState, TreeResult result);
}

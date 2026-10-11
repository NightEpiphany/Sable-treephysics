package com.moigferdsrte.treephysics.mixin.walk_through_leaves;

import com.moigferdsrte.treephysics.index.TreePhysicsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Unique
    private boolean treephysics$reduceFallSpeed = false;
    @Unique
    private boolean treephysics$inLeaves = false;
    @Unique
    private BlockPos treephysics$lastBlockPos = BlockPos.ZERO;

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"))
    private void treephysics$baseTick(CallbackInfo ci) {
        BlockPos blockPos = this.blockPosition();
        if(!Objects.equals(this.treephysics$lastBlockPos, blockPos)) {
            this.treephysics$lastBlockPos = blockPos;

            BlockState state = this.getInBlockState();
            if(state.is(BlockTags.LEAVES)) {
                this.treephysics$reduceFallSpeed = true;
            }
        }
    }

    @Inject(method = "travel", at = @At(value = "HEAD"))
    private void treephysics$travel(Vec3 input, CallbackInfo ci) {
        if (this.treephysics$reduceFallSpeed) {
            if(this.fallDistance > 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1, 0.7, 1));
                this.fallDistance = (float) Math.max(0.0, this.fallDistance - 1.0f);
            }

            this.treephysics$reduceFallSpeed = false;
        }
    }

    @Override
    protected void onInsideBlock(@NonNull BlockState state) {
        super.onInsideBlock(state);
        this.treephysics$inLeaves |= state.is(BlockTags.LEAVES);
    }

    @Override
    public void move(@NonNull MoverType type, @NonNull Vec3 movement) {
        if(!this.noPhysics) {
            BlockState state = this.getInBlockState();
            this.treephysics$inLeaves |= state.is(BlockTags.LEAVES);
            if(this.treephysics$inLeaves) {
                double value = TreePhysicsConfig.LEAF_WALKING_SPEED.getAsDouble();
                movement = movement.multiply(value, 1, value);
                this.setSprinting(false);
            }
            this.treephysics$inLeaves = false;
        }
        super.move(type, movement);
    }

}

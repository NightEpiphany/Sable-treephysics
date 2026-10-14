package com.moigferdsrte.treephysics.mixin.walk_through_leaves;

import com.moigferdsrte.treephysics.index.TreePhysicsConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;

@Mixin(BlockCollisions.class)
public class BlockCollisionsMixin {

    @Unique
    private Entity treephysics$entity;

    @Inject(
        method = "<init>(Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;ZLjava/util/function/BiFunction;)V",
        at = @At("TAIL")
    )
    private void treephysics$init(CollisionGetter collisionGetter, Entity source, AABB box, boolean onlySuffocatingBlocks, BiFunction<?, ?, ?> resultProvider, CallbackInfo ci) {
        this.treephysics$entity = source;
    }

    @WrapOperation(method = "computeNext", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/shapes/CollisionContext;getCollisionShape(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/CollisionGetter;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/phys/shapes/VoxelShape;"))
    private VoxelShape treephysics$getCollisionShape(CollisionContext instance, BlockState state, CollisionGetter collisionGetter, BlockPos pos, Operation<VoxelShape> original) {
        if(this.treephysics$entity instanceof LivingEntity && TreePhysicsConfig.LEAF_WALKING_BEHAVIOR.get().allowWorld() && state.getBlock() instanceof LeavesBlock) {
            return Shapes.empty();
        }
        return original.call(instance, state, collisionGetter, pos);
    }

}

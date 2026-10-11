package com.moigferdsrte.treephysics.fabric.mixin;

import com.moigferdsrte.treephysics.event.CommonEvents;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @WrapOperation(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/BlockItem;place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;"))
    private InteractionResult treephysics$place(BlockItem instance, BlockPlaceContext context, Operation<InteractionResult> original) {
        InteractionResult result = original.call(instance, context);
        if(result != null && result.consumesAction()) {
            CommonEvents.entityPlace(context.getLevel(), context.getClickedPos());
        }
        return result;
    }

}

package com.moigferdsrte.treephysics.fabric.mixin;

import com.moigferdsrte.treephysics.event.CommonEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void treephysics$useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        CommonEvents.useItemOnBlock(context.getLevel(), context.getClickedPos(), () -> {
            cir.cancel();
            cir.setReturnValue(InteractionResult.PASS);
        });
    }

}

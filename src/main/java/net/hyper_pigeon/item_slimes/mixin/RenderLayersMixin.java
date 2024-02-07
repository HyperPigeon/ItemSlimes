package net.hyper_pigeon.item_slimes.mixin;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderLayers.class)
public class RenderLayersMixin {
    @Inject(method = "getItemLayer", at = @At("HEAD"), cancellable = true)
    private static void getItemLayerMixin(ItemStack stack, boolean direct, CallbackInfoReturnable<RenderLayer> cir) {
        if (stack.getNbt() != null && stack.getNbt().contains("isInSlime") && stack.getNbt().getBoolean("isInSlime")){
            cir.setReturnValue(RenderLayer.getTripwire());
        }
    }
}

package net.hyper_pigeon.item_slimes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(SlimeEntityRenderer.class)
public abstract class SlimeEntityRendererMixin extends MobEntityRenderer<SlimeEntity, SlimeEntityModel<SlimeEntity>> {

    public SlimeEntityRendererMixin(EntityRendererFactory.Context context, SlimeEntityModel<SlimeEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

//    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "net/minecraft/client/render/entity/SlimeEntityRenderer.addFeature (Lnet/minecraft/client/render/entity/feature/FeatureRenderer;)Z",shift = At.Shift.AFTER))
//    public void addItemRenderer(EntityRendererFactory.Context context, CallbackInfo ci){
//        this.addFeature(new SlimeAbsorbedItemFeatureRenderer<>(this, context.getItemRenderer()));
//    }

//    @Inject(method = "render(Lnet/minecraft/entity/mob/SlimeEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
//    public void renderItems(SlimeEntity slimeEntity, float f, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, CallbackInfo ci){
//        int seed = slimeEntity.getId();
//        ItemAbsorbingMob itemAbsorbingMob = (ItemAbsorbingMob) slimeEntity;
//
//        if(itemAbsorbingMob.getSlimeItems().size() > 0){
//            for(int index = 0; index < itemAbsorbingMob.getSlimeItems().size(); index++){
//                ItemStack itemStack = itemAbsorbingMob.getSlimeItems().get(index);
//
//                matrices.push();
//
//                matrices.translate(0, -1*(slimeEntity.getSize()/2) + 1, 0);
//
//                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.GROUND, light,  OverlayTexture.DEFAULT_UV,
//                        matrices,
//                        vertexConsumerProvider,
//                        slimeEntity.getWorld(),
//                        seed);
//                matrices.pop();
//
//            }
//        }
//    }

}

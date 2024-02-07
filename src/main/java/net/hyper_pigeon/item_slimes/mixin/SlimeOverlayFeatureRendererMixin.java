package net.hyper_pigeon.item_slimes.mixin;

import net.hyper_pigeon.item_slimes.ItemAbsorbingMob;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(SlimeOverlayFeatureRenderer.class)
public abstract class SlimeOverlayFeatureRendererMixin <T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, SlimeEntityModel<T>> {


    public SlimeOverlayFeatureRendererMixin(FeatureRendererContext<T, SlimeEntityModel<T>> context) {
        super(context);
    }

    @Inject(method =
            "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V"
    , at = @At(value = "INVOKE", target = " net/minecraft/client/MinecraftClient.getInstance ()Lnet/minecraft/client/MinecraftClient;", shift = At.Shift.AFTER))
    public void renderSlimeItems(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch, CallbackInfo ci){
        SlimeEntity slimeEntity = (SlimeEntity) livingEntity;
        int seed = slimeEntity.getId();
        ItemAbsorbingMob itemAbsorbingMob = (ItemAbsorbingMob) slimeEntity;



        if(itemAbsorbingMob.getSlimeItems().size() > 0){
            for(int index = 0; index < itemAbsorbingMob.getSlimeItems().size(); index++){

                ItemStack itemStack = itemAbsorbingMob.getSlimeItems().get(index);

                matrixStack.push();
                this.scale(slimeEntity,matrixStack,tickDelta);
                matrixStack.translate(index, slimeEntity.getSize()+1, 0);
                MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.GROUND, light,  OverlayTexture.DEFAULT_UV,
                        matrixStack,
                        vertexConsumerProvider,
                        slimeEntity.getWorld(),
                        seed);
                matrixStack.pop();

            }
        }
    }

//    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
//    at = @At(value = "INVOKE",target = "net/minecraft/client/render/VertexConsumerProvider.getBuffer (Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/render/VertexConsumer;", ordinal = 1))
//    public VertexConsumer changeRenderLayer(VertexConsumerProvider instance, RenderLayer renderLayer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LivingEntity livingEntity){
//        return instance.getBuffer(RenderLayer.getItemEntityTranslucentCull(this.getTexture(livingEntity)));
//    }

    protected void scale(SlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0F, 0.001F, 0.0F);
        float h = (float)slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(1/(j * h), 1/(1.0F / j * h), 1/(j * h));
    }

}

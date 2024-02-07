package net.hyper_pigeon.item_slimes.client.render.entity.feature;

import com.mojang.blaze3d.systems.RenderSystem;
import net.hyper_pigeon.item_slimes.ItemAbsorbingMob;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class SlimeAbsorbedItemFeatureRenderer <T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
    private final ItemRenderer itemRenderer;

    public SlimeAbsorbedItemFeatureRenderer(FeatureRendererContext<T, M> context, ItemRenderer itemRenderer) {
        super(context);
        this.itemRenderer = itemRenderer;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {


        SlimeEntity slimeEntity = (SlimeEntity) entity;
        int seed = slimeEntity.getId();
        ItemAbsorbingMob itemAbsorbingMob = (ItemAbsorbingMob) slimeEntity;
        Vec3d cameraClientPos = slimeEntity.getClientCameraPosVec(tickDelta);

        Box boundingBox = slimeEntity.getBoundingBox();
        Random random = new Random();



        if(itemAbsorbingMob.getSlimeItems().size() > 0){
            for(int index = 0; index < itemAbsorbingMob.getSlimeItems().size(); index++){
                random.setSeed(seed+index);
                double randomX = boundingBox.minX + random.nextDouble()*(boundingBox.maxX - boundingBox.minX);
                double randomY = boundingBox.minY + random.nextDouble()*(boundingBox.maxY - boundingBox.minY);
                double randomZ = boundingBox.minZ + random.nextDouble()*(boundingBox.maxZ - boundingBox.minZ);

                ItemStack itemStack = itemAbsorbingMob.getSlimeItems().get(index);

//                System.out.println(RenderLayers.getItemLayer(itemStack, true));

                matrices.push();
                this.scale(slimeEntity,matrices,tickDelta);
                matrices.translate(0, slimeEntity.getSize(), 0);
                this.itemRenderer.renderItem(itemStack, ModelTransformationMode.NONE, light,  OverlayTexture.DEFAULT_UV,
                        matrices,
                        vertexConsumers,
                        slimeEntity.getWorld(),
                        seed);
                matrices.pop();
            }
        }
    }

    protected void scale(SlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0F, 0.001F, 0.0F);
        float h = (float)slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(1/(j * h), 1/(1.0F / j * h), 1/(j * h));
    }


}

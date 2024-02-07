package net.hyper_pigeon.item_slimes.mixin;

import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.item_slimes.ItemAbsorbingMob;
import net.hyper_pigeon.item_slimes.networking.ItemSlimesNetworkingConstants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityTrackerEntry.class)
public class EntityTrackerEntryMixin {
    @Shadow
    @Final
    private Entity entity;

    @Inject(method = "startTracking", at = @At("TAIL"))
    private void onStartTracking(ServerPlayerEntity player, CallbackInfo ci) {
        if(this.entity instanceof SlimeEntity slime) {
            ItemAbsorbingMob itemAbsorbingMob = (ItemAbsorbingMob) slime;
            itemAbsorbingMob.getSlimeItems().forEach(itemStack -> {
                PacketByteBuf packetByteBuf = PacketByteBufs.create();
                packetByteBuf.writeInt(this.entity.getId());

                NbtCompound nbtCompound = new NbtCompound();
                itemStack.writeNbt(nbtCompound);
                packetByteBuf.writeNbt(nbtCompound);

                ServerPlayNetworking.send(player, ItemSlimesNetworkingConstants.ABSORB_ITEM, packetByteBuf);
            });
        }
    }
}

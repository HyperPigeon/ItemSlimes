package net.hyper_pigeon.item_slimes.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.hyper_pigeon.item_slimes.ItemAbsorbingMob;
import net.hyper_pigeon.item_slimes.networking.ItemSlimesNetworkingConstants;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

@net.fabricmc.api.Environment(net.fabricmc.api.EnvType.CLIENT)
public class ItemSlimesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(ItemSlimesNetworkingConstants.ABSORB_ITEM, (client, handler, buf, responseSender) -> {
            int entityId = buf.readInt();
            NbtCompound nbtCompound = buf.readNbt();
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
            itemStack.getOrCreateNbt().putBoolean("isInSlime", true);
            client.execute(() -> {
                assert client.world != null;
                SlimeEntity slime = (SlimeEntity) client.world.getEntityById(entityId);
                ItemAbsorbingMob itemAbsorbingMob = (ItemAbsorbingMob) slime;
                if(itemAbsorbingMob != null) {
                    itemAbsorbingMob.addAbsorbedItem(itemStack);
                }
            });

        });
    }
}

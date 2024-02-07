package net.hyper_pigeon.item_slimes;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;

public interface ItemAbsorbingMob {

    int getMaxItems();

    ArrayList<ItemStack> getSlimeItems();

    void addAbsorbedItem(ItemStack itemStack);

    void addAbsorbedItem(ItemStack itemStack, ServerPlayerEntity player);
}

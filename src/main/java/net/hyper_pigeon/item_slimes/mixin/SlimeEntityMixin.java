package net.hyper_pigeon.item_slimes.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.hyper_pigeon.item_slimes.ItemAbsorbingMob;
import net.hyper_pigeon.item_slimes.networking.ItemSlimesNetworkingConstants;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends MobEntity implements ItemAbsorbingMob {
    protected SlimeEntityMixin(EntityType<? extends MobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract int getSize();

    @Shadow @Nullable public abstract EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt);

    private ArrayList<ItemStack> slimeItems;

    public int getMaxItems() {
        return getSize();
    }

    public ArrayList<ItemStack> getSlimeItems(){
        return this.slimeItems;
    }

    public void addAbsorbedItem(ItemStack itemStack){
        this.slimeItems.add(itemStack);
        if(!getWorld().isClient()){
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(this.getId());

            NbtCompound nbtCompound = new NbtCompound();
            itemStack.writeNbt(nbtCompound);
            nbtCompound.putBoolean("isInSlime", true);
            packetByteBuf.writeNbt(nbtCompound);

            for (ServerPlayerEntity player : PlayerLookup.tracking((SlimeEntity)(Object)(this))) {
                ServerPlayNetworking.send(player, ItemSlimesNetworkingConstants.ABSORB_ITEM, packetByteBuf);
            }
        }
    }

    public void addAbsorbedItem(ItemStack itemStack, ServerPlayerEntity player){
        this.slimeItems.add(itemStack);
        if(!getWorld().isClient()){
            PacketByteBuf packetByteBuf = PacketByteBufs.create();
            packetByteBuf.writeInt(this.getId());

            NbtCompound nbtCompound = new NbtCompound();
            itemStack.writeNbt(nbtCompound);
            packetByteBuf.writeNbt(nbtCompound);

            ServerPlayNetworking.send(player, ItemSlimesNetworkingConstants.ABSORB_ITEM, packetByteBuf);
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void initSlimeItems(EntityType entityType, World world, CallbackInfo ci){
        slimeItems = new ArrayList<>();
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void onWriteCustomDataToNbt(NbtCompound nbt, CallbackInfo ci){
        if(!getSlimeItems().isEmpty()) {
            NbtList nbtList = new NbtList();
            getSlimeItems().forEach(itemStack -> {
                NbtCompound nbtCompound = new NbtCompound();
                itemStack.writeNbt(nbtCompound);
                nbtList.add(nbtCompound);
            });
            nbt.put("Items", nbtList);
        }
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void onReadCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci){
        NbtList nbtList = nbt.getList("Items", 10);
        if(!nbtList.isEmpty()){
            nbtList.forEach(nbtElement -> {
                ItemStack itemStack = ItemStack.fromNbt((NbtCompound) nbtElement);
                addAbsorbedItem(itemStack);
            });
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci){
        if ((this.horizontalCollision || this.verticalCollision) && this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            List<ItemEntity> intersectingItemEntities = this.getWorld().getNonSpectatingEntities(ItemEntity.class, this.getBoundingBox().expand(0.1));
            if(!intersectingItemEntities.isEmpty()){
                intersectingItemEntities.forEach(itemEntity -> {
                    if(getSlimeItems().size() < getMaxItems()){
                        addAbsorbedItem(itemEntity.getStack());
                        itemEntity.remove(RemovalReason.DISCARDED);
                    }
                });
            }
        }
    }





}

package com.craftix.hostile_humans.network.message;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageHumanData {

    protected final CompoundTag data;
    protected final String humanMobEntityUUID;

    public MessageHumanData(String humanMobEntityUUID, CompoundTag data) {
        this.humanMobEntityUUID = humanMobEntityUUID;
        this.data = data;
    }

    public static void handle(MessageHumanData message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> handlePacket(message)));
        context.setPacketHandled(true);
    }

    public static void handlePacket(MessageHumanData message) {
        //  HumansClientData.load(message.getData());
    }

    public CompoundTag getData() {
        return this.data;
    }

    public String getHHFollowerUUID() {
        return this.humanMobEntityUUID;
    }
}

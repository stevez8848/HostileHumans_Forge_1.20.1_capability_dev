package com.craftix.hostile_humans.network.message;

import com.craftix.hostile_humans.entity.data.HumansClientData;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record MessageHumansData(CompoundTag data) {

    public static void handle(MessageHumansData message,
                              Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> handlePacket(message)));
        context.setPacketHandled(true);
    }

    public static void handlePacket(MessageHumansData message) {
        HumansClientData.load(message.data());
    }
}

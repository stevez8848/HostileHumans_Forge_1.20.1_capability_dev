package com.mrh0.createaddition.network;

import com.mrh0.createaddition.CreateAddition;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ObservePacket implements CustomPacketPayload {
	private BlockPos pos;
	private int node;
	
	public ObservePacket(BlockPos pos, int node) {
		this.pos = pos;
		this.node = node;
	}
	
	public static void encode(ObservePacket packet, FriendlyByteBuf tag) {
        tag.writeBlockPos(packet.pos);
        tag.writeInt(packet.node);
    }
	
	public static ObservePacket decode(FriendlyByteBuf buf) {
		ObservePacket scp = new ObservePacket(buf.readBlockPos(), buf.readInt());
        return scp;
    }
	
	public static void handle(ObservePacket pkt, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			try {
				ServerPlayer player = (ServerPlayer) ctx.player();
				
				if (player != null) {
					sendUpdate(pkt, player);
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
//		ctx.get().setPacketHandled(true);
	}
	
	private static void sendUpdate(ObservePacket pkt, ServerPlayer player) {
		BlockEntity te = (BlockEntity) player.level().getBlockEntity(pkt.pos);
        if (te != null) {
        	if(te instanceof IObserveTileEntity) {
	        	IObserveTileEntity ote = (IObserveTileEntity) te;
	        	ote.onObserved(player, pkt);
	            Packet<ClientGamePacketListener> supdatetileentitypacket = te.getUpdatePacket();
	            if (supdatetileentitypacket != null)
	                player.connection.send(supdatetileentitypacket);
        	}
        }
    }
	
	private static int cooldown = 0;
	public static void tick() {
		cooldown--;
		if(cooldown < 0)
			cooldown = 0;
	}
	
	public static boolean send(BlockPos pos, int node) {
		if(cooldown > 0)
			return false;
		cooldown = 10;
		PacketDistributor.sendToServer(new ObservePacket(pos, node));
		return true;
	}
	
	public BlockPos getPos() {
		return pos;
	}
	
	public int getNode() {
		return node;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("cas", "obs"));
	}
}


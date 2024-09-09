package com.binaris.forgotten_tome_menu.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ForgottenTomePacket() {
    public static void encode(ForgottenTomePacket packet, FriendlyByteBuf buffer) {
        // :p
    }

    public static ForgottenTomePacket decode(FriendlyByteBuf buffer) {
        return new ForgottenTomePacket();
    }

    public static void handle(ForgottenTomePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || player.level.isClientSide) return;

            BlockPos position;
            if(player.getRespawnPosition() != null) {
                position = player.getRespawnPosition();
            } else {
                position = player.getLevel().getSharedSpawnPos();
            }

            player.teleportTo(position.getX(), position.getY(), position.getZ());
        });
        ctx.get().setPacketHandled(true);
    }
}

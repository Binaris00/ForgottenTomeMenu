package com.binaris.forgotten_tome_menu.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
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

            ResourceLocation rl = new ResourceLocation("beyondtheend", "the_forgotten_realm");
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, rl);
            ServerLevel target = player.server.getLevel(key);
            if (target == null) {
                target = player.server.getLevel(Level.OVERWORLD);
            }

            // Coordenadas fijas
            double x = 0.5D;
            double y = 142.0D;
            double z = 0.5D;

            ChunkPos cpos = new ChunkPos(0 >> 4, 0 >> 4);
            target.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, cpos, 1, player.getId());

            player.teleportTo(target, x, y, z, player.getYRot(), player.getXRot());
        });
        ctx.get().setPacketHandled(true);
}
}

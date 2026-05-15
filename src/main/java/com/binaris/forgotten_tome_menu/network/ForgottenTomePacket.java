package com.binaris.forgotten_tome_menu.network;

import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
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
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("ender_journey", "the_forgotten_realm"));
            ServerLevel dimension = player.server.getLevel(key);
            Vec3 tpPos = new Vec3(0.0, 79.0, 0.0);
            if (dimension == null) {
                dimension = player.server.getLevel(Level.OVERWORLD);
                tpPos = Vec3.atCenterOf(dimension.getSharedSpawnPos());
            }
            player.teleportTo(dimension, tpPos.x, tpPos.y, tpPos.z, 90.0F, 0.0F);
        });
        ctx.get().setPacketHandled(true);
}
}

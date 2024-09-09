package com.binaris.forgotten_tome_menu.network;

import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1.0.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(ForgottenTomeMod.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals);

    private static int id = 0;

    public static void init() {
        INSTANCE.registerMessage(id++, ForgottenTomePacket.class, ForgottenTomePacket::encode, ForgottenTomePacket::decode, ForgottenTomePacket::handle);
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

}

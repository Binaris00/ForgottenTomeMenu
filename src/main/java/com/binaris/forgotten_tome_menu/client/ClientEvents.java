package com.binaris.forgotten_tome_menu.client;

import com.binaris.forgotten_tome_menu.ForgottenButtonHandler;
import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import com.binaris.forgotten_tome_menu.config.ConfigClient;
import com.binaris.forgotten_tome_menu.mixin.AbstractContainerScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ContainerScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ForgottenTomeMod.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onGuiRender(ContainerScreenEvent.Render.Background event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (ForgottenButtonHandler.bookButton == null || minecraft.player == null
                || minecraft.level == null || !(minecraft.screen instanceof InventoryScreen screen)) return;

        if (minecraft.level.getGameTime() % 5 == 0) {
            ForgottenButtonHandler.bookButton.visible = true;
            int leftPos = ((AbstractContainerScreenAccessor)screen).getLeftPos();
            int topPos = ((AbstractContainerScreenAccessor)screen).getTopPos();
            ForgottenButtonHandler.bookButton.setPosition(leftPos + ConfigClient.button_x, topPos + ConfigClient.button_y);
        }
    }
}

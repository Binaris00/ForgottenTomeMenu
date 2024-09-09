package com.binaris.forgotten_tome_menu.mixin;

import com.binaris.forgotten_tome_menu.ForgottenButtonHandler;
import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import com.binaris.forgotten_tome_menu.TeleportingState;
import com.binaris.forgotten_tome_menu.config.ConfigClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    public InventoryScreenMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/InventoryScreen;addRenderableWidget(Lnet/minecraft/client/gui/components/events/GuiEventListener;)Lnet/minecraft/client/gui/components/events/GuiEventListener;", shift = At.Shift.AFTER))
    private void forgotten_tome_init(CallbackInfo ci) {
        ResourceLocation resourceLocation;
        if(ForgottenTomeMod.damageGroup.containsKey(this.getMinecraft().player.getUUID()) && this.getMinecraft().level.getGameTime() - ForgottenTomeMod.damageGroup.get(this.getMinecraft().player.getUUID()) <= 600){
            resourceLocation = ForgottenButtonHandler.CANCEL_BUTTON_TEXTURE;
        }else{
            resourceLocation = ForgottenButtonHandler.BUTTON_TEXTURE;
        }

        ForgottenButtonHandler.bookButton = new ImageButton(leftPos + ConfigClient.button_x, topPos + ConfigClient.button_y, 20, 18, 0, 0, 19, resourceLocation, clicked -> {
            Minecraft minecraft1 = Minecraft.getInstance();

            if(minecraft1.player != null){
                ((TeleportingState) minecraft1.player).forgottenTomeMenu$setTeleporting(true);
            }
        });

        ForgottenButtonHandler.bookButton.visible = true;
        this.addRenderableWidget(ForgottenButtonHandler.bookButton);
    }

    // Error (?)
    @Inject(method = "(Lnet/minecraft/client/gui/components/Button;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/Button;setPosition(II)V", shift = At.Shift.AFTER))
    private void forgotten_tome_pos(Button button, CallbackInfo cb) {
        if (ForgottenButtonHandler.bookButton != null) ForgottenButtonHandler.bookButton.setPosition(this.leftPos + ConfigClient.button_x, this.topPos + ConfigClient.button_y);
    }
}

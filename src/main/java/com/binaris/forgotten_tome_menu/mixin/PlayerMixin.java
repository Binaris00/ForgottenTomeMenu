package com.binaris.forgotten_tome_menu.mixin;

import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import com.binaris.forgotten_tome_menu.TeleportingState;
import com.binaris.forgotten_tome_menu.network.ForgottenTomePacket;
import com.binaris.forgotten_tome_menu.network.NetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements TeleportingState {
    @Unique
    int teleportTicks = 0;
    @Unique
    boolean isTeleporting = false;
    @Unique
    BlockPos originalPos = null;
    @Unique
    Player player = (Player)(Object)this;


    @Inject(method = "tick", at = @At("HEAD"))
    public void forgotten_tome_tick(CallbackInfo ci){
        if(forgottenTomeMenu$isTeleporting()){
            if(originalPos == null){
                originalPos = player.blockPosition();
            }

            if(!player.blockPosition().equals(originalPos)){
                player.displayClientMessage(Component.literal("You can't move while teleporting!").withStyle(ChatFormatting.RED), true);
                isTeleporting = false;
                teleportTicks = 0;
                originalPos = null;
                return;
            }

            if(ForgottenTomeMod.damageGroup.containsKey(player.getUUID())){
                if(player.level.getGameTime() - ForgottenTomeMod.damageGroup.get(player.getUUID()) <= 600){
                    player.displayClientMessage(Component.literal("You can't teleport while in combat!").withStyle(ChatFormatting.RED), true);
                    isTeleporting = false;
                    teleportTicks = 0;
                    originalPos = null;
                    return;
                }
            }
            teleportTicks++;
            player.displayClientMessage(Component.literal("Teleporting... ").withStyle(ChatFormatting.GREEN), true);
            if(teleportTicks >= 100){
                player.displayClientMessage(Component.literal("Teleported!").withStyle(ChatFormatting.GREEN), true);
                originalPos = null;
                isTeleporting = false;
                teleportTicks = 0;
                NetworkHandler.sendToServer(new ForgottenTomePacket());
            }
        }
    }

    @Override
    public int forgottenTomeMenu$teleportTicks() {
        return teleportTicks;
    }

    @Override
    public boolean forgottenTomeMenu$isTeleporting() {
        return isTeleporting;
    }


    @Override
    public boolean forgottenTomeMenu$setTeleporting(boolean teleporting) {
        return isTeleporting = teleporting;
    }
}

package com.binaris.forgotten_tome_menu.mixin;

import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import com.binaris.forgotten_tome_menu.TeleportingState;
import com.binaris.forgotten_tome_menu.network.ForgottenTomePacket;
import com.binaris.forgotten_tome_menu.network.NetworkHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

    @Unique
    boolean wasInCombat = false;

    @Unique
    private static final ResourceLocation FORGOTTEN_REALM_ID =
            new ResourceLocation("ender_journey", "the_forgotten_realm");

    @Unique
    private static final int TOTAL_TICKS = 100;

    @Unique
    private static final int BAR_LENGTH = 20;

    @Unique
    private static final char FILLED_CHAR  = '█';

    @Unique
    private static final char EMPTY_CHAR   = '░';


    @Unique
    private Component buildProgressBar(int ticks) {
        int filled = (int) Math.round((double) ticks / TOTAL_TICKS * BAR_LENGTH);
        filled = Math.max(0, Math.min(filled, BAR_LENGTH));

        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < BAR_LENGTH; i++) {
            bar.append(i < filled ? FILLED_CHAR : EMPTY_CHAR);
        }
        bar.append("] ");
        bar.append((int) Math.round((double) ticks / TOTAL_TICKS * 100));
        bar.append("%");

        return Component.literal("⬛ ")
                .withStyle(ChatFormatting.DARK_GRAY)
                .append(
                    Component.translatable("forgotten_tome.teleport.progress")
                        .withStyle(ChatFormatting.GREEN)
                )
                .append(
                    Component.literal(" " + bar)
                        .withStyle(ChatFormatting.GREEN)
                );
    }


    @Inject(method = "tick", at = @At("HEAD"))
    public void forgotten_tome_tick(CallbackInfo ci){

        boolean isInCombat = ForgottenTomeMod.damageGroup.containsKey(player.getUUID())
                && player.level.getGameTime() - ForgottenTomeMod.damageGroup.get(player.getUUID()) <= 600;

        if (wasInCombat && !isInCombat && player.level.isClientSide) {
            player.sendSystemMessage(
                    Component.translatable("forgotten_tome.teleport.combat_ready")
                            .withStyle(ChatFormatting.GREEN)
            );
            player.level.playLocalSound(
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.NOTE_BLOCK_PLING,
                    SoundSource.PLAYERS,
                    1.0F, 2.0F, false
            );
        }

        wasInCombat = isInCombat;

        if(forgottenTomeMenu$isTeleporting()){

            // Teleport instantáneo si el jugador ya está en The Forgotten Realm
            boolean isInForgottenRealm = player.level.dimension().location()
                    .equals(FORGOTTEN_REALM_ID);

            if (isInForgottenRealm) {
                player.displayClientMessage(
                        Component.translatable("forgotten_tome.teleport.success")
                                .withStyle(ChatFormatting.GREEN),
                        true
                );
                originalPos = null;
                isTeleporting = false;
                teleportTicks = 0;
                NetworkHandler.sendToServer(new ForgottenTomePacket());
                return;
            }

            if(originalPos == null){
                originalPos = player.blockPosition();
            }

            if(!player.blockPosition().equals(originalPos)){
                player.displayClientMessage(
                        Component.translatable("forgotten_tome.teleport.move_blocked")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                isTeleporting = false;
                teleportTicks = 0;
                originalPos = null;
                return;
            }

            if(ForgottenTomeMod.damageGroup.containsKey(player.getUUID())){
                if(player.level.getGameTime() - ForgottenTomeMod.damageGroup.get(player.getUUID()) <= 600){
                    player.displayClientMessage(
                            Component.translatable("forgotten_tome.teleport.combat_blocked")
                                    .withStyle(ChatFormatting.RED),
                            true
                    );
                    isTeleporting = false;
                    teleportTicks = 0;
                    originalPos = null;
                    return;
                }
            }

            teleportTicks++;

            player.displayClientMessage(buildProgressBar(teleportTicks), true);

            if(teleportTicks >= TOTAL_TICKS){
                player.displayClientMessage(
                        Component.translatable("forgotten_tome.teleport.success")
                                .withStyle(ChatFormatting.GREEN),
                        true
                );

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

package com.binaris.forgotten_tome_menu.config;

import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ForgottenTomeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigCommon {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue BEFORE_COOLDOWN = BUILDER
            .comment("Cooldown when you use the button (In ticks) (20 ticks = 1 second)")
            .defineInRange("before_cooldown", 100, 0, Integer.MAX_VALUE);

    private static final ForgeConfigSpec.IntValue AFTER_COOLDOWN = BUILDER
            .comment("Cooldown after you use the button (In ticks) (20 ticks = 1 second)")
            .defineInRange("after_cooldown", 100, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static int beforeCooldown;
    public static int afterCooldown;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        beforeCooldown = BEFORE_COOLDOWN.get();
        afterCooldown = AFTER_COOLDOWN.get();
    }
}

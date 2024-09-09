package com.binaris.forgotten_tome_menu.config;

import com.binaris.forgotten_tome_menu.ForgottenTomeMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = ForgottenTomeMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigClient {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue BOOK_BUTTON_X = BUILDER
            .comment("Button X position")
            .defineInRange("book_button_x", 110, -500, 500);

    private static final ForgeConfigSpec.IntValue BOOK_BUTTON_Y = BUILDER
            .comment("Button Y position")
            .defineInRange("book_button_y", 61, -500, 500);


    public static int button_x;
    public static int button_y;

    public static final ForgeConfigSpec SPEC = BUILDER.build();


    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        button_x = BOOK_BUTTON_X.get();
        button_y = BOOK_BUTTON_Y.get();
    }
}

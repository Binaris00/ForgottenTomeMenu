package com.binaris.forgotten_tome_menu.config;

import java.util.Locale;
import net.minecraftforge.common.ForgeConfigSpec;

public class ConfigCommon {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.ConfigValue<String> TARGET_DIMENSION = BUILDER
            .define("target_dimension", "minecraft:overworld");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String getTargetDimensionId() {
        String v = TARGET_DIMENSION.get();
        return normalizeDimensionId(v);
    }

    public static String normalizeDimensionId(String raw) {
        if (raw == null) return "minecraft:overworld";
        String s = raw.trim().toLowerCase(Locale.ROOT);
        if (s.isEmpty()) return "minecraft:overworld";
        if (!s.contains(":")) s = "minecraft:" + s;
        if (s.equals("minecraft:nether")) s = "minecraft:the_nether";
        if (s.equals("minecraft:end")) s = "minecraft:the_end";
        if (s.equals("minecraft:overworld")) s = "minecraft:overworld";
        return s;
    }
}

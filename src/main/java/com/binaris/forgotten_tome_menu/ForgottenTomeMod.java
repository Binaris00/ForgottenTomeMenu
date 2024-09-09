package com.binaris.forgotten_tome_menu;

import com.binaris.forgotten_tome_menu.config.ConfigClient;
import com.binaris.forgotten_tome_menu.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.UUID;

@Mod(ForgottenTomeMod.MODID)
public class ForgottenTomeMod {
    public static final String MODID = "forgotten_tome_menu";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final HashMap<UUID, Long> damageGroup = new HashMap<>();


    public ForgottenTomeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigClient.SPEC, "forgotten-tome-menu-client.toml");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(NetworkHandler::init);
    }

    @SubscribeEvent
    public void LivingAttackEvent(LivingAttackEvent event){
        if(event.getEntity() instanceof Player player){
            damageGroup.remove(player.getUUID());
            damageGroup.put(player.getUUID(), player.level.getGameTime());
        }
    }
}

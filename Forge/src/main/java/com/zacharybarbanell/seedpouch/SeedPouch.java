package com.zacharybarbanell.seedpouch;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Mod(Constants.MOD_ID)
public class SeedPouch {
    
    public SeedPouch() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::onClientSetup);

        bind(Registry.ITEM_REGISTRY, CommonClass::registerItems);
        bind(Registry.MENU_REGISTRY, CommonClass::registerMenuTypes);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(
                () -> {
                    MenuScreens.register(CommonClass.SEED_POUCH_MENU, PouchScreen::new);
                    MenuScreens.register(CommonClass.SAPLING_POUCH_MENU, PouchScreen::new);
                    MenuScreens.register(CommonClass.DIRT_POUCH_MENU, PouchScreen::new);
                });
    }

    @SubscribeEvent
    public void onPickupItem(EntityItemPickupEvent event) {
        if (PouchItem.onPickupItem(event.getItem(), event.getEntity())) {
            event.setCanceled(true);
        }
    }

    private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
        FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
            if (registry.equals(event.getRegistryKey())) {
                source.accept((t, rl) -> event.register(registry, rl, () -> t));
            }
        });
    }
}
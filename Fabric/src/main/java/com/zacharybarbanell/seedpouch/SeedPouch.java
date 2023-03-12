package com.zacharybarbanell.seedpouch;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

public class SeedPouch implements ModInitializer {
    @Override
    public void onInitialize() {
        CommonClass.registerItems(bind(Registry.ITEM));
        CommonClass.registerMenuTypes(bind(Registry.MENU));
    }

    private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
        return (t, id) -> Registry.register(registry, id, t);
    }
}

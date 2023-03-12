package com.zacharybarbanell.seedpouch;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;

public class SeedPouchClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        MenuScreens.register(CommonClass.SEED_POUCH_MENU, PouchScreen::new);
        MenuScreens.register(CommonClass.SAPLING_POUCH_MENU, PouchScreen::new);
        MenuScreens.register(CommonClass.DIRT_POUCH_MENU, PouchScreen::new);
    }
}

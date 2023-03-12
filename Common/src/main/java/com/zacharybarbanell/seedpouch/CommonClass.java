package com.zacharybarbanell.seedpouch;

import com.zacharybarbanell.seedpouch.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

import static com.zacharybarbanell.seedpouch.Constants.MOD_ID;

public class CommonClass {
    public static final Item SEED_POUCH_ITEM = new PouchItem(
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1),
                    () -> StreamSupport.stream(Registry.ITEM.getTagOrEmpty(Services.PLATFORM.getSeedTag()).spliterator(), false)
                            .map(Holder::value).toList());

    public static final MenuType<PouchContainer> SEED_POUCH_MENU = PouchContainer.getMenuType((PouchItem) SEED_POUCH_ITEM);

    public static final Item SAPLING_POUCH_ITEM = new PouchItem(
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1),
                    () -> StreamSupport.stream(Registry.ITEM.getTagOrEmpty(ItemTags.SAPLINGS).spliterator(), false)
                            .map(Holder::value).toList());

    public static final MenuType<PouchContainer> SAPLING_POUCH_MENU = PouchContainer.getMenuType((PouchItem) SAPLING_POUCH_ITEM);

    public static final Item DIRT_POUCH_ITEM = new PouchItem(
                    new Item.Properties().tab(CreativeModeTab.TAB_MISC).stacksTo(1),
                    () -> Arrays.asList(Items.DIRT));

    public static final MenuType<PouchContainer> DIRT_POUCH_MENU = PouchContainer.getMenuType((PouchItem) DIRT_POUCH_ITEM);

    public static void registerItems(BiConsumer<Item, ResourceLocation> consumer) {
        consumer.accept(SEED_POUCH_ITEM, id("seed_pouch"));
        consumer.accept(SAPLING_POUCH_ITEM, id("sapling_pouch"));
        consumer.accept(DIRT_POUCH_ITEM, id("dirt_pouch"));
    }

    public static void registerMenuTypes(BiConsumer<MenuType<?>, ResourceLocation> consumer) {
        consumer.accept(SEED_POUCH_MENU, id("seed_pouch"));
        consumer.accept(SAPLING_POUCH_MENU, id("sapling_pouch"));
        consumer.accept(DIRT_POUCH_MENU, id("dirt_pouch"));
    }

    public static ResourceLocation id(String s) {
        return new ResourceLocation(MOD_ID, s);
    }
}
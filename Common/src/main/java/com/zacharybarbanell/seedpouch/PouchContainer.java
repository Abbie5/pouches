/*
 * This class is adapted from part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package com.zacharybarbanell.seedpouch;

import com.zacharybarbanell.seedpouch.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import com.zacharybarbanell.seedpouch.helper.SlotLocked;

//import vazkii.botania.client.gui.SlotLocked;
//import vazkii.botania.common.block.BotaniaDoubleFlowerBlock;
//import vazkii.botania.common.block.BotaniaFlowerBlock;
//import vazkii.botania.common.item.BotaniaItems;
//import vazkii.botania.common.item.FlowerPouchItem;

public class PouchContainer extends AbstractContainerMenu {
	private static Map<PouchItem, MenuType<PouchContainer>> menuTypes = new HashMap<PouchItem, MenuType<PouchContainer>>();

	public static MenuType<PouchContainer> getMenuType(PouchItem item) {
		if (menuTypes.containsKey(item)) {
			return menuTypes.get(item);
		} else {
			MenuType<PouchContainer> type = Services.PLATFORM.createMenuType(
					(Integer windowId, Inventory inv, FriendlyByteBuf buf) -> {
						InteractionHand hand = buf.readBoolean() ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
						return new PouchContainer(windowId, inv, inv.player.getItemInHand(hand), item);
					});
			menuTypes.put(item, type);
			return type;
		}
	}

	private final ItemStack bag;
	public final PouchItem bagType;
	public final Container bagInv;
	public final int containerRows;

	public PouchContainer(int windowId, Inventory playerInv, ItemStack bag, PouchItem bagType) {
		super(PouchContainer.getMenuType(bagType), windowId);

		this.bag = bag;
		this.bagType = bagType;
		this.containerRows = (bagType.getSize() - 1) / 9 + 1;
		if (!playerInv.player.level.isClientSide) {
			bagInv = bagType.getInventory(bag);
		} else {
			bagInv = new SimpleContainer(bagType.getSize());
		}

		// TODO
		for (int i = 0; i < bagType.getSize(); i++) {
			int row = i / 9;
			int col = i % 9;
			int width = row == containerRows - 1 ? (bagType.getSize() - 1) % 9 + 1 : 9;
			addSlot(new Slot(bagInv, i, 8 + col * 18 + (9 - width) * 9, 18 + row * 18) {
				@Override
				public boolean mayPlace(@NotNull ItemStack stack) {
					return bagType.isValid(this.getContainerSlot(), stack);
				}
			});
		}

		int offset = (this.containerRows - 4) * 18;

		// player inventory
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 9; ++col) {
				addSlot(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 103 + row * 18 + offset));
			}
		}

		// player hotbar
		for (int i = 0; i < 9; ++i) {
			if (playerInv.getItem(i) == bag) {
				addSlot(new SlotLocked(playerInv, i, 8 + i * 18, 161 + offset));
			} else {
				addSlot(new Slot(playerInv, i, 8 + i * 18, 161 + offset));
			}
		}

	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		ItemStack main = player.getMainHandItem();
		ItemStack off = player.getOffhandItem();
		return !main.isEmpty() && main == bag || !off.isEmpty() && off == bag;
	}

	@NotNull
	@Override
	public ItemStack quickMoveStack(Player player, int slotIndex) {

		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = slots.get(slotIndex);

		if (slot.hasItem()) {
			ItemStack itemstack1 = slot.getItem();
			itemstack = itemstack1.copy();

			if (slotIndex < this.bagType.getSize()) {
				if (!moveItemStackTo(itemstack1, this.bagType.getSize(), this.bagType.getSize() + 36, true)) {
					return ItemStack.EMPTY;
				}
			} else {
				Integer slotId = this.bagType.getSlots().inverse().get(itemstack.getItem());
				if (slotId != null) {
					Slot destination = slots.get(slotId);
					if (destination.mayPlace(itemstack) && !moveItemStackTo(itemstack1, slotId,
							slotId + 1, true)) {
						return ItemStack.EMPTY;
					}
				}
			}

			if (itemstack1.isEmpty()) {
				slot.set(ItemStack.EMPTY);
			} else {
				slot.setChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;

	}

}

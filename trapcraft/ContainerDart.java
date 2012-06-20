package net.minecraft.trapcraft;

import net.minecraft.src.*;

public class ContainerDart extends Container {
	ItemStack dart;

	public ContainerDart(InventoryPlayer inventoryPlayer, ItemStack dart, int slotDartX, int slotDartY) {
		addSlot(new SlotDart(new InventoryDart(dart), 0, slotDartX, slotDartY));
		for (int i = 0; i < 3; i++) {
			for (int k = 0; k < 9; k++) {
				addSlot(new Slot(inventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			addSlot(new Slot(inventoryPlayer, j, 8 + j * 18, 142));
		}
	}

	protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}

}

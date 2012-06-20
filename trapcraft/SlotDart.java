package net.minecraft.trapcraft;

import net.minecraft.src.*;

public class SlotDart extends Slot {
	public SlotDart(IInventory inventory, int slotIndex, int xDisplayPos, int yDisplayPos) {
		super(inventory, slotIndex, xDisplayPos, yDisplayPos);
	}
	
	@Override
	public boolean isItemValid(ItemStack itemStack) {
		return itemStack.getItem() == Item.potion;
	}
}

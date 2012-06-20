package net.minecraft.trapcraft;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class InventoryDart implements IInventory {
	ItemStack dart;
	
	public InventoryDart(ItemStack dart) {
		this.dart = dart;
	}
	
	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i != 0)
			return null;
		return dart;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		if (dart != null)
			return dart.splitStack(j);
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (i != 0)
			return null;
		return dart;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i != 0)
			return;
		dart = itemstack;
	}

	@Override
	public String getInvName() {
		return "container.dart";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void onInventoryChanged() {
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {
	}

	@Override
	public void closeChest() {
	}

}

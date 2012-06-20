package net.minecraft.trapcraft;

import net.minecraft.src.*;

public class ItemDart extends Item {
	public ItemDart(int par1) {
		super(par1);
		iconIndex = Item.arrow.getIconFromDamage(0); //TODO: Make icon!
		setHasSubtypes(true);
        setMaxDamage(0);
		setMaxStackSize(1);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
		InventoryPlayer ip = ModLoader.getMinecraftInstance().thePlayer.inventory;
		int damage = itemStack.getItemDamage();
		if (damage != 0){
			ItemStack potion = new ItemStack(Item.potion, 1);
			potion.setItemDamage(damage);
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiDart(ip, potion, itemStack));
		}
		else {
			ModLoader.getMinecraftInstance().displayGuiScreen(new GuiDart(ip, null, itemStack));
		}
		return itemStack;
	}

}

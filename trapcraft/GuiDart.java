package net.minecraft.trapcraft;

import net.minecraft.src.GuiContainer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;

import org.lwjgl.opengl.GL11;

public class GuiDart extends GuiContainer {
	ItemStack dart;
	public GuiDart(InventoryPlayer player, ItemStack potion, ItemStack dart) {
		super(new ContainerDart(player, potion, 80, 34));
		this.dart = dart;
	}

	@Override
	public void onGuiClosed() {
		ItemStack potion = inventorySlots.getSlot(0).getStack();
		if (potion != null){
			dart.setItemDamage(potion.getItemDamage());
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer() {
		fontRenderer.drawString("Load Dart With Potion", 20, 6, 0x404040);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		int i = mc.renderEngine.getTexture("/trapcraft/images/dartgui.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

        //drawTexturedModalRect(j + 79, k + 34, 176, 14, 1, 16);
	}
}

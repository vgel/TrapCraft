package net.minecraft.trapcraft;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;

public class GuiOPPButton extends GuiButton {
	boolean clicked;
	float r, g, b;
	
	public GuiOPPButton(int par1, int par2, int par3, int par4, int par5, String par6Str) {
		super(par1, par2, par3, par4, par5, par6Str);
		r = 1.0F;
		g = 1.0F;
		b = 1.0F;
	}
	
	public void setColor(float r, float g, float b){
		this.r = r;
		this.b = b;
		this.g = g;
	}
	
	public void buttonClicked() {
		clicked = !clicked;
		enabled = true;
	}

	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if (!drawButton) {
			return;
		}
		FontRenderer fontrenderer = par1Minecraft.fontRenderer;
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1Minecraft.renderEngine.getTexture("/gui/gui.png"));
		GL11.glColor4f(r, g, b, 1.0F);
		boolean flag = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + field_52008_a && par3 < yPosition + field_52007_b;
		int i = getHoverState(flag);
		drawTexturedModalRect(xPosition, yPosition, 0, 46 + i * 20, field_52008_a / 2, field_52007_b);
		drawTexturedModalRect(xPosition + field_52008_a / 2, yPosition, 200 - field_52008_a / 2, 46 + i * 20, field_52008_a / 2, field_52007_b);
		mouseDragged(par1Minecraft, par2, par3);
		int j = 0xe0e0e0;

		if (!enabled) {
			j = 0xffa0a0a0;
		} else if (flag) {
			j = 0xffffa0;
		}
		if (clicked && enabled) {
			j = 0xFFFF00;
		}

		drawCenteredString(fontrenderer, displayString, xPosition + field_52008_a / 2, yPosition + (field_52007_b - 8) / 2, j);
	}

}

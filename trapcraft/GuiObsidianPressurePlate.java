package net.minecraft.trapcraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.src.EntityList;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiOptions;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_TrapCraft;

public class GuiObsidianPressurePlate extends GuiScreen {	
	static String unCamelCase(String camel){
		String unCamel = "";
		String currentWord = "";
		for (int i = 0; i < camel.length(); i++){
			char c = camel.charAt(i);
			if (charUpper(c)){
				if (i + 1 < camel.length() && charUpper(camel.charAt(i + 1)) || i + 1 == camel.length()){
					currentWord += c;
				}
				else {
					unCamel += (unCamel.equals("") ? "" : " ") + currentWord;
					currentWord = "" + c;
				}
			}
			else {
				currentWord += c;
			}
		}
		unCamel += (unCamel.equals("") ? "" : " ") + currentWord;
		return unCamel;
	}
	
	static boolean charUpper(char c){
		return Character.isAlphabetic(c) && Character.isUpperCase(c);
	}
	
	TileEntityObsidianPressurePlate te;
	Map<GuiOPPButton, String> buttonToEntityString;
	
	public GuiObsidianPressurePlate(TileEntityObsidianPressurePlate te) {
		this.te = te;
		buttonToEntityString = new HashMap<GuiOPPButton, String>();
	}
	
	@Override
	public void initGui() {
		super.initGui();
		addButtons();
	}
	
	@Override
	protected void actionPerformed(GuiButton guibutton) {
		super.actionPerformed(guibutton);
		if (guibutton instanceof GuiOPPButton){
			((GuiOPPButton)guibutton).buttonClicked();
		}
	}
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
		te.triggering.clear();
		for (Object o : controlList){
			if (o instanceof GuiOPPButton && ((GuiOPPButton)o).id < 100000 && ((GuiOPPButton)o).clicked){
				String eString = buttonToEntityString.get(o);
				te.addTrigger(eString);
			}
		}
	}
	
	void addButtons(){
		int x = 0;
		int y = 20;
		int i = 0;
		for (String s : mod_TrapCraft.strToClass.keySet()){
			GuiOPPButton b = new GuiOPPButton(i++, x, y, 100, 20, unCamelCase(s));
			buttonToEntityString.put(b, s);
			if (te.triggers(s)){
				b.clicked = true;
			}
			b.setColor(1.0F, 1.0F, 0.5F);
			controlList.add(b);
			y += 20;
			if (y > height){
				y = 20;
				x += 100;
				if (x > width){
					return;
				}
			}
		}
		controlList.add(new GuiOPPButton(100000, 0, 0, 100, 20, "Select All"){
			@Override
			public void buttonClicked() {
				for (Object o : controlList){
					if (o instanceof GuiOPPButton){
						GuiOPPButton b = (GuiOPPButton)o;
						if (b.id < 100000)
							b.clicked = true;
					}
				}
			}
		});
		controlList.add(new GuiOPPButton(100001, 100, 0, 100, 20, "Clear All"){
			@Override
			public void buttonClicked() {
				for (Object o : controlList){
					if (o instanceof GuiOPPButton){
						GuiOPPButton b = (GuiOPPButton)o;
						if (b.id < 100000)
							b.clicked = false;
					}
				}
			}
		});
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(ModLoader.getMinecraftInstance().fontRenderer, "Select Triggering Entities", width / 2, 5, 0xefefef);
		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
}

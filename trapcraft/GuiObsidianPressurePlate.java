package net.minecraft.trapcraft;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.src.EntityList;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ModLoader;

public class GuiObsidianPressurePlate extends GuiScreen {
	static Map<String, String> camelToRegular;
	static Map<String, Class<?>> strToClass;
	static {
		camelToRegular = new HashMap<String, String>();
		try {
			strToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "b");
		} catch (Exception e){ //would use multicatch but not 1.7 :(
			try {
				strToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "stringToClassMapping");
			} catch (Exception e1){
				e.printStackTrace();
			}
		}
		for (String s : strToClass.keySet()){
			camelToRegular.put(s, unCamelCase(s));
		}
	}
	
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
	
	public GuiObsidianPressurePlate() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		for (int i = 0; i < camelToRegular.values().size(); i++){
			drawCenteredString(ModLoader.getMinecraftInstance().fontRenderer, camelToRegular.get(i), width / 2, 110, 0xffcccc);
		}
	}
}

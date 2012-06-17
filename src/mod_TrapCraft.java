package net.minecraft.trapcraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BaseMod;
import net.minecraft.src.Block;

public class mod_TrapCraft extends BaseMod {
	final String VERSION = "0.1 (1.2.5)";
	final String AUTHORS = "Rotten194 and lastrano";
	final Properties defaults = new Properties();
	
	Properties props;
	Block obsidianPressurePlate;
	
	public mod_TrapCraft() {
		System.out.println("?????");
		setupDefaults();
		try {
			loadProperties();
		} catch (IOException e){
			System.err.println("TrapCraft couldn't load properties!");
			System.err.println("Using defaults, if they were changed your world may become corrupted!!!!");
			System.err.println(getVersion());
			e.printStackTrace();
		}
	}
	
	void setupDefaults(){
		defaults.put("obsidian_pressure_plate_id", 1816); //generated by random.org
	}
	
	void loadProperties() throws IOException {
		props = new Properties(defaults);
		File f = new File(Minecraft.getMinecraftDir(), "TrapCraft.props");
		if (!f.exists()){
			f.createNewFile();
			props.store(new FileWriter(f), getPropertiesHeader());
		}
	}
	
	String getPropertiesHeader(){
		String fact = randFact[new Random().nextInt(randFact.length)];
		return getVersion() + "\nRandom fact of this config: " + fact;
	}
	
	@Override
	public String getVersion() {
		return String.format("TrapCraft version %s by %s", VERSION, AUTHORS);
	}

	@Override
	public void load() {
		
	}
	
	final String[] randFact = new String[]{
			"Gastric Flu can cause projectile vomiting!",
			"Dalmations are born without spots, which come in about a week after birth.",
			"October 10, 2006 was declared \"Tom Cruise Day\" in Japan.",
			"Isaac Asimov is the only author with a book in every Dewey-decimal category.",
			"\"Buffalo buffalo Buffalo buffalo buffalo buffalo Buffalo buffalo.\" is a gramatically correct English sentence!",
			"50% of people in Kentucky getting married for the first time are teenagers. Keep it classy, Kentucky.",
			"Lightning strikes about 6000 times per minute."
	};
}

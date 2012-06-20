package net.minecraft.src;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.trapcraft.*;

public class mod_TrapCraft extends BaseMod {
	final String VERSION = "0.1 (1.2.5)";
	final String AUTHORS = "Rotten194 and lastrano";
	final Properties defaults = new Properties();
	
	Properties props;
	public static Block obsidianPressurePlate;
	public static Block fireBox;
	public static Item itemDart;
	private static GuiScreen creativeInventory;
	
	public static Map<String, Class<?>> strToClass;
	public static Map<Class<?>, String> classToStr;
	
	public mod_TrapCraft() {
		setupDefaults();
		try {
			loadProperties();
		} catch (Exception e){
			System.err.println("TrapCraft couldn't load properties!");
			System.err.println("Using defaults, if they were changed your world may become corrupted!!!!");
			System.err.println(getVersion());
			e.printStackTrace();
		}
		System.out.println(props.keys());
		
		// -- OBSIDIAN PRESSUREPLATE: --
			int idOPP = Integer.parseInt((String)props.get("obsidian_pressure_plate_id"));
			System.out.println("ObsidianplateID=" + idOPP);
			obsidianPressurePlate = (new BlockObsidianPressurePlate(idOPP, Block.obsidian.blockIndexInTexture, Material.rock)).setHardness(0.5F).setStepSound(Block.soundStoneFootstep).setBlockName("obPressurePlate").setRequiresSelfNotify();
			ModLoader.registerBlock(obsidianPressurePlate);
			ModLoader.addRecipe(new ItemStack(obsidianPressurePlate, 1), new Object[]{
				"   ",
				"OOO",
				"RRR",
				'O', Block.obsidian,
				'R', Item.redstone
			});
			ModLoader.addRecipe(new ItemStack(obsidianPressurePlate, 1), new Object[]{
				"OOO",
				"RRR",
				"   ",
				'O', Block.obsidian,
				'R', Item.redstone
			});
			ModLoader.addName(obsidianPressurePlate, "Obsidian Pressure Plate");
			ModLoader.registerTileEntity(TileEntityObsidianPressurePlate.class, "tileEntityObsidianPressurePlate");
		// -- END --
		
		// -- FIREBOX: --
			int idFB = Integer.parseInt((String)props.get("firebox_id"));
			int idTF = Integer.parseInt((String)props.get("trapfire_id"));
			System.out.println("FireboxID=" + idFB);
			System.out.println("TrapfireID=" + idTF);
			boolean fireBoxSwitch = (Boolean) defaults.get("firebox_replace_and_save_block");
			fireBox = (new BlockFirebox(idFB, idTF, fireBoxSwitch, Material.rock)).setBlockName("firebox").setHardness(0.5F).setStepSound(Block.soundStoneFootstep);
			fireBox.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/trapcraft/images/firebox_top.png");
			ModLoader.registerBlock(fireBox);
			ModLoader.addRecipe(new ItemStack(fireBox, 1), new Object[]{
				"CNC",
				"CFC",
				"CRC",
				'C', Block.stone, 'N', Block.fenceIron, 'F', Item.flintAndSteel, 'R', Item.redstone
			});
			ModLoader.addName(fireBox, "Firebox");
		// -- END --
			
		// -- DART --
			int dartId = Integer.parseInt((String)props.getProperty("dart_id"));
			itemDart = (new ItemDart(dartId)).setItemName("item.dart");
			ModLoader.addName(itemDart, "Dart");
			
	}
	
	void setupDefaults(){
		defaults.put("obsidian_pressure_plate_id", 142);
		defaults.put("firebox_id", 143);
		defaults.put("trapfire_id", 144);
		defaults.put("firebox_replace_and_save_block", false);
		defaults.put("dart_id", "11969");
	}
	
	void loadProperties() throws IOException {
		props = new Properties();
		for (Entry e : defaults.entrySet()){
			props.setProperty(e.getKey().toString(), e.getValue().toString());
		}
		File f = new File(Minecraft.getMinecraftDir(), "TrapCraft.props");
		if (!f.exists()){
			f.createNewFile();
		}
		else {
			props.load(new FileReader(f));
		}
		props.store(new FileWriter(f), getPropertiesHeader());
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
		// Hooks for adding to creative:
		ModLoader.setInGameHook(this, true, false);
		ModLoader.setInGUIHook(this, true, false);
	}
	
	@Override
	public boolean dispenseEntity(World world, double d, double d1, double d2, int i, int j, ItemStack itemstack) {
		if (itemstack.getItem() == itemDart){
			EntityDart entitydart = new EntityDart(world, d, d1, d2);
			entitydart.setDartStack(itemstack);
            entitydart.setArrowHeading(i, 0.10000000149011612D, j, 1.1F, 6F);
            entitydart.doesArrowBelongToPlayer = true;
            world.spawnEntityInWorld(entitydart);
            world.playAuxSFX(1002, (int)d, (int)d1, (int)d2, 0);
			return true;
		}
		return false;
	}
	
	@Override
	public void addRenderer(Map map) {
		map.put(EntityDart.class, new RenderDart());
	}
	
	@Override
	public void modsLoaded() {
		super.modsLoaded();
		try {
			strToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "b");
		} catch (Exception e){ //would use multicatch but not 1.7 :(
			try {
				strToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "stringToClassMapping");
			} catch (Exception e1){
				e.printStackTrace();
			}
		}
		try {
			classToStr = (Map<Class<?>, String>)ModLoader.getPrivateValue(EntityList.class, null, "c");
		} catch (Exception e){ //would use multicatch but not 1.7 :(
			try {
				classToStr = (Map<Class<?>, String>)ModLoader.getPrivateValue(EntityList.class, null, "classToStringMapping");
			} catch (Exception e1){
				e.printStackTrace();
			}
		}
		strToClass.put("Player", EntityPlayer.class);
		classToStr.put(EntityPlayer.class, "Player");
	}
	
	public boolean onTickInGame(float f, Minecraft minecraft)
	{
		if(minecraft.currentScreen == null)
		{
			creativeInventory = null;
		}
		return true;
	}
	
	public boolean onTickInGUI(float f, Minecraft minecraft, GuiScreen guiscreen)
	{
		if((guiscreen instanceof GuiContainerCreative) && !(creativeInventory instanceof GuiContainerCreative) && !minecraft.theWorld.isRemote)
		{
			Container container = ((GuiContainer)guiscreen).inventorySlots;
			List list = ((ContainerCreative)container).itemList;
			// ADD TO CREATIVE HERE:
				list.add(new ItemStack(obsidianPressurePlate, 1, 0));
				list.add(new ItemStack(fireBox, 1, 0));
				list.add(new ItemStack(itemDart, 1, 0));
		}
		creativeInventory = guiscreen;
		return true;
	}
	
	final String[] randFact = new String[]{
			"Gastric Flu can cause projectile vomiting!",
			"Dalmations are born without spots, which come in about a week after birth.",
			"October 10, 2006 was declared \"Tom Cruise Day\" in Japan.",
			"Isaac Asimov is the only author with a book in every Dewey-decimal category.",
			"\"Buffalo buffalo Buffalo buffalo buffalo buffalo Buffalo buffalo.\" is a gramatically correct English sentence!",
			"50% of people in Kentucky getting married for the first time are teenagers. Keep it classy, Kentucky.",
			"Lightning strikes about 6000 times per minute.",
			"Knights would often fight other knights by holding their sword by the blade and swinging it like a hammer"
	};
}

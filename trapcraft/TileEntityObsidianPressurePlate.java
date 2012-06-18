package net.minecraft.trapcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.*;

public class TileEntityObsidianPressurePlate extends TileEntity {
	static Map<Class<?>, String> classToString;
	static Map<String, Class<?>> stringToClass;
	static {
		try {
			classToString = (Map<Class<?>, String>)ModLoader.getPrivateValue(EntityList.class, null, "c");
		} catch (Exception e){ //would use multicatch but not 1.7 :(
			try {
				classToString = (Map<Class<?>, String>)ModLoader.getPrivateValue(EntityList.class, null, "classToStringMapping");
			} catch (Exception e1){
				e.printStackTrace();
			}
		}
		try {
			stringToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "b");
		} catch (Exception e){ //would use multicatch but not 1.7 :(
			try {
				stringToClass = (Map<String, Class<?>>)ModLoader.getPrivateValue(EntityList.class, null, "stringToClassMapping");
			} catch (Exception e1){
				e.printStackTrace();
			}
		}
	}
	
	List<String> triggering;
	
	public TileEntityObsidianPressurePlate() {
		triggering = new ArrayList<String>();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagList list = nbtTagCompound.getTagList("trigger_list");
		for (int i = 0; i < list.tagCount(); i++){
			NBTTagString s = (NBTTagString)list.tagAt(i);
			triggering.add(s.data);
		}
		System.out.println(triggering);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList list = new NBTTagList();
		for (String s : triggering){
			System.out.println("Saving trigger: " + s);
			list.appendTag(new NBTTagString(s, s));
		}
		nbtTagCompound.setTag("trigger_list", list);
	}
	
	public void addTrigger(String s){
		triggering.add(s);
	}
	
	public void addTrigger(Entity e){
		addTrigger(classToString.get(e.getClass()));
	}
	
	public boolean triggers(String o){
		return triggering.contains(o);
	}
	
	public boolean triggers(Entity e){
		return triggers(classToString.get(e.getClass()));
	}
}

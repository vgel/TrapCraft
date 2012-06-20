package net.minecraft.trapcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.src.*;

public class TileEntityObsidianPressurePlate extends TileEntity {
	List<Class<?>> triggering;
	
	public TileEntityObsidianPressurePlate() {
		triggering = new ArrayList<Class<?>>();
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagList list = nbtTagCompound.getTagList("trigger_list");
		for (int i = 0; i < list.tagCount(); i++){
			NBTTagString s = (NBTTagString)list.tagAt(i);
			triggering.add(mod_TrapCraft.strToClass.get(s.data));
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagList list = new NBTTagList();
		for (Class<?> clazz : triggering){
			list.appendTag(new NBTTagString(mod_TrapCraft.classToStr.get(clazz), mod_TrapCraft.classToStr.get(clazz)));
		}
		nbtTagCompound.setTag("trigger_list", list);
	}
	
	public void addTrigger(String s){
		triggering.add(mod_TrapCraft.strToClass.get(s));
	}
	
	public void addTrigger(Entity e){
		addTrigger(mod_TrapCraft.classToStr.get(e.getClass()));
	}
	
	public boolean triggers(String o){
		Class<?> c = mod_TrapCraft.strToClass.get(o);
		for (Class<?> c1 : triggering){
			if (c1.isAssignableFrom(c)){
				return true;
			}
		}
		return false;
	}
	
	public boolean triggers(Entity e){
		Class<?> c = e.getClass();
		for (Class<?> c1 : triggering){
			if (c1.isAssignableFrom(c)){
				return true;
			}
		}
		return false;
	}
}

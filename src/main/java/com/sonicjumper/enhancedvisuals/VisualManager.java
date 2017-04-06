package com.sonicjumper.enhancedvisuals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.HashMapList;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;

public class VisualManager {
	
	public static HashMapList<VisualCategory, Visual> visuals = new HashMapList<>();
	
	private static HashMap<VisualType, VisualPersistent> persistentVisuals = new HashMap<>();
	
	private static ArrayList<Visual> defaultVisuals = new ArrayList<>();
	
	/**There can be only one persistent visual for each type**/
	public static void addPersistentVisual(VisualPersistent visual)
	{
		if(!visual.type.isEnabled())
			return ;
		persistentVisuals.put(visual.type, visual);
		visuals.add(visual.type.category, visual);
	}
	
	public static void addVisual(Visual visual)
	{
		if(!visual.type.isEnabled())
			return ;
		VisualPersistent persitent = persistentVisuals.get(visual.type);
		if(persitent != null)
		{
			persitent.addVisual(visual);
		}else{
			defaultVisuals.add(visual);
			visuals.add(visual.type.category, visual);
		}
	}
	
	public static void clearAllVisuals()
	{
		visuals.clear();
		persistentVisuals.clear();
		defaultVisuals.clear();
	}
	
	public static void onTick(@Nullable EntityPlayer player)
	{
		for (Iterator<VisualPersistent> iterator = persistentVisuals.values().iterator(); iterator.hasNext();) {
			VisualPersistent visual = iterator.next();
			visual.onTick(player);
		}
		
		int i = 0;
		while(i < defaultVisuals.size())
		{
			Visual visual = defaultVisuals.get(i);
			visual.onTick(player);
			if(visual.hasFinished())
			{
				defaultVisuals.remove(i);
				visuals.removeValue(visual.type.category, visual);
			}else
				i++;
		}
	}
	
}

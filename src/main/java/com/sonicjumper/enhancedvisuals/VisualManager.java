package com.sonicjumper.enhancedvisuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.utils.HashMapList;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualFadeOut;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;

public class VisualManager {
	
	public static HashMapList<VisualCategory, Visual> visuals = new HashMapList<>();
	
	private static HashMap<VisualType, VisualPersistent> persistentVisuals = new HashMap<>();
	
	private static ArrayList<Visual> defaultVisuals = new ArrayList<>();
	
	public static VisualPersistent getPersitentVisual(VisualType type)
	{
		return persistentVisuals.get(type);
	}
	
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
	
	public static void addVisualsWithShading(VisualType vt, int num, int minTime, int maxTime)
	{
		addVisualsWithShading(vt, 1, num, minTime, maxTime, Color.WHITE);
	}
	
	public static void addVisualWithShading(VisualType vt, float intensity, int minTime, int maxTime, Color color)
	{
		if(intensity <= 0)
			return ;
		addVisual(new VisualFadeOut(vt, intensity, minTime, maxTime, color));
	}
	
	public static void addVisualsWithShading(VisualType vt, float intensity, int num, int minTime, int maxTime, Color color)
	{
		if(intensity <= 0)
			return ;
		for(int i = 0; i < num; i++) {
			addVisualWithShading(vt, intensity, minTime, maxTime, color);
		}
	}
	
	public static void createVisualFromDamage(VisualType type, float damage, EntityLivingBase bleedingEntity) {
		createVisualFromDamageAndDistance(type, damage, bleedingEntity, 1.0D);
	}

	public static void createVisualFromDamageAndDistance(VisualType type, float damage, EntityLivingBase bleedingEntity, double distanceSqToEntity) {
		if(damage <= 0.0F) {
			return;
		}

		double distance = Math.sqrt(distanceSqToEntity);
		distance = Math.max(distance, 1.0D);
		double distMultiplier = 1.0D / (distance / 2.0D);

		float rate = 0.0F;
		float health = bleedingEntity.getHealth() - damage;
		if (health > 12.0F) {
			rate = 1.0F;
		}
		if ((health <= 12.0F) && (health > 8.0F)) {
			rate = 1.5F;
		}
		if ((health <= 8.0F) && (health > 4.0F)) {
			rate = 2.0F;
		}
		if ((health <= 4.0F) && (health > 0.0F)) {
			rate = 2.5F;
		}
		if (health <= 0.0F) {
			rate = 3.0F;
		}
		int splats = (int)(damage * rate * distMultiplier);
		//System.out.println("Created " + splats + " splats of type " + type.getName());
		if((type.equals(VisualType.splatter)) || (type.equals(VisualType.slash)) || (type.equals(VisualType.pierce)) || (type.equals(VisualType.impact)))
		{
			if(bleedingEntity instanceof EntityCreeper) {
				addVisualsWithShading(type, 1, splats, 500, 1500, new Color(0.0F, 0.4F, 0.0F, 0.7F));
			} else if(bleedingEntity instanceof EntitySkeleton) {
				addVisualsWithShading(type, 1, splats, 500, 1500, new Color(0.1F, 0.1F, 0.1F, 0.7F));
			} else if(bleedingEntity instanceof EntitySquid) {
				addVisualsWithShading(type, 1, splats, 500, 1500, new Color(0.0F, 0.0F, 0.2F, 0.7F));
			} else {
				addVisualsWithShading(type, 1, splats, 500, 1500, new Color(0.3F, 0.01F, 0.01F, 0.7F));
			}
		}
		else if (type.equals(VisualType.dust)) {
			addVisualsWithShading(type, 1, splats * 20, 100, 1000, new Color(0.2F, 0.2F, 0.2F, 1.0F));
		}
	}
	
	public static void resetAllVisuals()
	{
		for (Iterator<VisualPersistent> iterator = persistentVisuals.values().iterator(); iterator.hasNext();) {
			VisualPersistent visual = iterator.next();
			visual.reset();
		}
		
		for (Iterator<Visual> iterator = defaultVisuals.iterator(); iterator.hasNext();) {
			Visual visual = iterator.next();
			visuals.removeValue(visual.type.category, visual);
		}
		defaultVisuals.clear();
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

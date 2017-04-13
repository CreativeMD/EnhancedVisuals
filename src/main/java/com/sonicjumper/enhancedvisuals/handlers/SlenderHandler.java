package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;

public class SlenderHandler extends VisualHandler {

	public SlenderHandler() {
		super("slender", "slenderman effect if you are near an enderman");
	}
	
	public float defaultIntensity = 0F;
	public float slenderDistanceFactor = 0.25F;
	public float maxIntensity = 0.3F;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", name, defaultIntensity, 0, 1, "the default intensity");
		slenderDistanceFactor = config.getFloat("slenderDistanceFactor", name, slenderDistanceFactor, 0, 10000, "intensity = distance * slenderDistanceFactor");
		maxIntensity = config.getFloat("maxIntensity", name, maxIntensity, 0, 1, "maximum intensity");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		VisualPersistent visual = VisualManager.getPersitentVisual(VisualType.slender);
		if(visual != null && player != null)
		{
			boolean angryNearby = false;
		    float modifier = 0.0F;
		    double d0 = player.posX;
		    double d1 = player.posY;
		    double d2 = player.posZ;
		    
		    AxisAlignedBB box = player.getEntityBoundingBox();
		    box = box.expand(16, 16, 16);
		    
		    EntityEnderman mob = (EntityEnderman) player.worldObj.findNearestEntityWithinAABB(EntityEnderman.class, box, player);
		    if(mob != null)
		    {
		    	angryNearby = true;
		    	float distModifier = (float) (1.0F / Math.pow(Math.sqrt(Math.pow(d0 - mob.posX, 2) + Math.pow(d1 - mob.posY, 2) + Math.pow(d2 - mob.posZ, 2)) / 3.0D, 2));
		    	if (distModifier > modifier)
		    	{
		    		modifier = distModifier;
		    		if (modifier > 3.5F) {
		    			modifier = 3.5F;
		    		}
		    	}
		    }
		    
		    if (angryNearby)
		    	visual.setIntensity(Math.max(defaultIntensity, Math.min(maxIntensity, slenderDistanceFactor * modifier)));
		    else
		    	visual.setIntensity(defaultIntensity);
		}
	}
	
}

package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;

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
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("defaultIntensity", new FloatSegment("defaultIntensity", 0F, 0, 1).setToolTip("the default intensity"));
		branch.registerElement("slenderDistanceFactor", new FloatSegment("slenderDistanceFactor", 0.25F, 0, 10000).setToolTip("intensity = distance * slenderDistanceFactor"));
		branch.registerElement("maxIntensity", new FloatSegment("maxIntensity", 0.3F, 0, 1).setToolTip("maximum intensity"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		defaultIntensity = (Float) branch.getValue("defaultIntensity");
		slenderDistanceFactor = (Float) branch.getValue("slenderDistanceFactor");
		maxIntensity = (Float) branch.getValue("maxIntensity");
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
		    box = box.grow(16, 16, 16);
		    
		    EntityEnderman mob = (EntityEnderman) player.world.findNearestEntityWithinAABB(EntityEnderman.class, box, player);
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

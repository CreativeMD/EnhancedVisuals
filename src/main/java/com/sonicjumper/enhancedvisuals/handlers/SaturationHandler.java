package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;

public class SaturationHandler extends VisualHandler {

	public SaturationHandler() {
		super("saturation", "saturation depending on hunger");
	}
	
	public float defaultSaturation = 1F;
	public float minSaturation = 0F;
	public float fadeFactor = 0.0005F;
	public int maxFoodLevelEffected = 8;
	public int minFoodLevelEffected = 2;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultSaturation = config.getFloat("defaultSaturation", name, defaultSaturation, 0, 10000, "the default/max saturation");
		minSaturation = config.getFloat("minSaturation", name, minSaturation, 0, 10000, "lowest saturation");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "saturation += fadeFactor per Tick");
		maxFoodLevelEffected = config.getInt("maxFoodLevelEffected", name, maxFoodLevelEffected, 0, 20, "the maximum point saturation is effected");
		minFoodLevelEffected = config.getInt("minFoodLevelEffected", name, minFoodLevelEffected, 0, 20, "the minimum point saturation is effected");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		VisualPersistent visual = VisualManager.getPersitentVisual(VisualType.desaturate);
		if(visual != null)
		{
			float aimedSaturation = defaultSaturation;
			
			if(player != null)
			{
				if(player.getFoodStats().getFoodLevel() <= maxFoodLevelEffected)
				{
					float leftFoodInSpan = player.getFoodStats().getFoodLevel()-minFoodLevelEffected;
					float spanLength = maxFoodLevelEffected-minFoodLevelEffected;
					aimedSaturation = Math.max(minSaturation, (leftFoodInSpan/spanLength)*defaultSaturation);
				}
			}
			
			if(visual.getIntensity() < aimedSaturation)
				visual.setIntensity(Math.min(visual.getIntensity()+fadeFactor, aimedSaturation));
			else if(visual.getIntensity() > aimedSaturation)
				visual.setIntensity(Math.max(visual.getIntensity()-fadeFactor, aimedSaturation));
		}
	}

}

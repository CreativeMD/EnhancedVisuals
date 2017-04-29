package com.sonicjumper.enhancedvisuals.addon.toughasnails;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;


public class ThirstHandler extends VisualHandler {

	public ThirstHandler() {
		super("thirst", "addon for ToughAsNailsAddon");
	}

	public float defaultIntensity = 0F;
	public float maxIntensity = 5;
	public float fadeFactor = 0.05F;
	public int maxThirstLevelEffected = 8;
	public int minThirstLevelEffected = 2;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", name, defaultIntensity, 0, 10000, "the default/max thirst");
		maxIntensity = config.getFloat("maxIntensity", name, maxIntensity, 0, 10000, "lowest thirst");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "thirst += fadeFactor per Tick");
		maxThirstLevelEffected = config.getInt("maxThirstLevelEffected", name, maxThirstLevelEffected, 0, 20, "the maximum point thirst is effected");
		minThirstLevelEffected = config.getInt("minThirstLevelEffected", name, minThirstLevelEffected, 0, 20, "the minimum point thirst is effected");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		VisualPersistent visual = VisualManager.getPersitentVisual(ToughAsNailsAddon.focus);
		if(visual != null)
		{
			float aimedSaturation = defaultIntensity;
			if(player != null)
			{
				if(((IThirst) player.getCapability(TANCapabilities.THIRST, null)).getThirst() <= maxThirstLevelEffected)
				{
					float leftFoodInSpan = ((IThirst) player.getCapability(TANCapabilities.THIRST, null)).getThirst()-minThirstLevelEffected;
					float spanLength = maxThirstLevelEffected-minThirstLevelEffected;
					aimedSaturation = (1-(leftFoodInSpan/spanLength))*maxIntensity;
				}
			}
			
			if(visual.getIntensity() < aimedSaturation)
				visual.setIntensity(Math.min(visual.getIntensity()+fadeFactor, aimedSaturation));
			else if(visual.getIntensity() > aimedSaturation)
				visual.setIntensity(Math.max(visual.getIntensity()-fadeFactor, aimedSaturation));
		}
	}
	
}

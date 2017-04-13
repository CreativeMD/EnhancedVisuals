package com.sonicjumper.enhancedvisuals.addon.toughasnails;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;
import toughasnails.api.stat.capability.ITemperature;

public class TemperatureHandler extends VisualHandler {

	public TemperatureHandler() {
		super("freeze", "addon for ToughAsNailsAddon");
	}
	
	public float defaultIntensity = 0F;
	public float mediumIntensity = 0.2F;
	public float maxIntensity = 0.4F;
	public float fadeFactor = 0.005F;
	public int maxThirstLevelEffected = 8;
	public int minThirstLevelEffected = 2;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", name, defaultIntensity, 0, 10000, "");
		maxIntensity = config.getFloat("maxIntensity", name, maxIntensity, 0, 10000, "if it's hot or icy");
		mediumIntensity = config.getFloat("mediumIntensity", name, mediumIntensity, 0, 10000, "if it's warm or cool");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "alpha += fadeFactor per Tick");
	}
	
	private static Temperature defaultTemperature = new Temperature(12);
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		VisualPersistent freeze = VisualManager.getPersitentVisual(ToughAsNailsAddon.freeze);
		VisualPersistent heat = VisualManager.getPersitentVisual(ToughAsNailsAddon.heat);
		if(freeze != null && heat != null)
		{
			float aimedHeat = defaultIntensity;
			float aimedFreeze = defaultIntensity;
			Temperature temp = defaultTemperature;
			
			if(player != null)
				temp = ((ITemperature) player.getCapability(TANCapabilities.TEMPERATURE, null)).getTemperature();
			
			if(temp == null)
				temp = defaultTemperature;
			
			TemperatureRange range = temp.getRange();
			switch(range)
			{
			case ICY:
				aimedHeat = 0;
				aimedFreeze = maxIntensity;
				break;
			case COOL:
				aimedHeat = 0;
				aimedFreeze = mediumIntensity*temp.getRangeDelta(true);
				
				break;
			case MILD:
				aimedHeat = defaultIntensity;
				aimedFreeze = defaultIntensity;
				break;
			case WARM:
				aimedHeat = mediumIntensity*temp.getRangeDelta(false);
				aimedFreeze = 0;
				break;
			case HOT:
				aimedHeat = maxIntensity;
				aimedFreeze = 0;
				break;			
			}
			
			
			if(freeze.getIntensity() < aimedFreeze)
				freeze.setIntensity(Math.min(freeze.getIntensity()+fadeFactor, aimedFreeze));
			else if(freeze.getIntensity() > aimedFreeze)
				freeze.setIntensity(Math.max(freeze.getIntensity()-fadeFactor, aimedFreeze));
			
			if(heat.getIntensity() < aimedHeat)
				heat.setIntensity(Math.min(heat.getIntensity()+fadeFactor, aimedHeat));
			else if(heat.getIntensity() > aimedHeat)
				heat.setIntensity(Math.max(heat.getIntensity()-fadeFactor, aimedHeat));
		}
	}
	
}

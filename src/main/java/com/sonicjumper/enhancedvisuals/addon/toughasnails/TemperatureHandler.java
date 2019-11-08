package com.sonicjumper.enhancedvisuals.addon.toughasnails;

import javax.annotation.Nullable;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;
import team.creative.enhancedvisuals.client.VisualManager;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;

public class TemperatureHandler extends VisualHandler {
	
	public TemperatureHandler() {
		super("freeze", "addon for ToughAsNailsAddon");
	}
	
	public float defaultIntensity = 0F;
	public float mediumIntensity = 0.2F;
	public float maxIntensity = 0.4F;
	public float fadeFactor = 0.005F;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", name, defaultIntensity, 0, 10000, "");
		maxIntensity = config.getFloat("maxIntensity", name, maxIntensity, 0, 10000, "if it's hot or icy");
		mediumIntensity = config.getFloat("mediumIntensity", name, mediumIntensity, 0, 10000, "if it's warm or cool");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "alpha += fadeFactor per Tick");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("defaultIntensity", new FloatSegment("defaultIntensity", 0F, 0, 10000).setToolTip("the default idensity if everything is fine"));
		branch.registerElement("maxIntensity", new FloatSegment("maxIntensity", 0.4F, 0, 10000).setToolTip("if it's hot or icy"));
		branch.registerElement("mediumIntensity", new FloatSegment("mediumIntensity", 0.2F, 0, 10000).setToolTip("if it's warm or cool"));
		branch.registerElement("fadeFactor", new FloatSegment("fadeFactor", 0.005F, 0, 10000).setToolTip("alpha += fadeFactor per Tick"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		defaultIntensity = (Float) branch.getValue("defaultIntensity");
		maxIntensity = (Float) branch.getValue("maxIntensity");
		mediumIntensity = (Float) branch.getValue("mediumIntensity");
		fadeFactor = (Float) branch.getValue("fadeFactor");
	}
	
	private static Temperature defaultTemperature = new Temperature(12);
	
	@Override
	public void onTick(@Nullable EntityPlayer player) {
		VisualPersistent freeze = VisualManager.getPersitentVisual(ToughAsNailsAddon.freeze);
		VisualPersistent heat = VisualManager.getPersitentVisual(ToughAsNailsAddon.heat);
		if (freeze != null && heat != null) {
			float aimedHeat = defaultIntensity;
			float aimedFreeze = defaultIntensity;
			Temperature temp = defaultTemperature;
			
			if (player != null)
				temp = ((ITemperature) player.getCapability(TANCapabilities.TEMPERATURE, null)).getTemperature();
			
			if (temp == null)
				temp = defaultTemperature;
			
			TemperatureRange range = temp.getRange();
			switch (range) {
			case ICY:
				aimedHeat = 0;
				aimedFreeze = maxIntensity;
				break;
			case COOL:
				aimedHeat = 0;
				aimedFreeze = mediumIntensity * temp.getRangeDelta(true);
				
				break;
			case MILD:
				aimedHeat = defaultIntensity;
				aimedFreeze = defaultIntensity;
				break;
			case WARM:
				aimedHeat = mediumIntensity * temp.getRangeDelta(false);
				aimedFreeze = 0;
				break;
			case HOT:
				aimedHeat = maxIntensity;
				aimedFreeze = 0;
				break;
			}
			
			if (freeze.getIntensity(1.0F) < aimedFreeze)
				freeze.setIntensity(Math.min(freeze.getIntensity(1.0F) + fadeFactor, aimedFreeze));
			else if (freeze.getIntensity(1.0F) > aimedFreeze)
				freeze.setIntensity(Math.max(freeze.getIntensity(1.0F) - fadeFactor, aimedFreeze));
			
			if (heat.getIntensity(1.0F) < aimedHeat)
				heat.setIntensity(Math.min(heat.getIntensity(1.0F) + fadeFactor, aimedHeat));
			else if (heat.getIntensity(1.0F) > aimedHeat)
				heat.setIntensity(Math.max(heat.getIntensity(1.0F) - fadeFactor, aimedHeat));
		}
	}
	
}

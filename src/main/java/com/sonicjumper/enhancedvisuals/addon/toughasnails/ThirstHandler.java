package com.sonicjumper.enhancedvisuals.addon.toughasnails;

import javax.annotation.Nullable;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;
import team.creative.enhancedvisuals.client.VisualManager;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.IThirst;

public class ThirstHandler extends VisualHandler {
	
	public ThirstHandler() {
		super("thirst", "addon for ToughAsNailsAddon");
	}
	
	public float defaultIntensity = 0F;
	public float maxIntensity = 5;
	public float fadeFactor = 0.05F;
	
	public int minThirstLevelEffected = 2;
	public int maxThirstLevelEffected = 8;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", name, defaultIntensity, 0, 10000, "the default/max thirst");
		maxIntensity = config.getFloat("maxIntensity", name, maxIntensity, 0, 10000, "lowest thirst");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "thirst += fadeFactor per Tick");
		
		minThirstLevelEffected = config.getInt("minThirstLevelEffected", name, minThirstLevelEffected, 0, 20, "the minimum point thirst is effected");
		maxThirstLevelEffected = config.getInt("maxThirstLevelEffected", name, maxThirstLevelEffected, 0, 20, "the maximum point thirst is effected");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("defaultIntensity", new FloatSegment("defaultIntensity", 0F, 0, 10000).setToolTip("the default/max thirst"));
		branch.registerElement("maxIntensity", new FloatSegment("maxIntensity", 5F, 0, 10000).setToolTip("lowest thirst"));
		branch.registerElement("fadeFactor", new FloatSegment("fadeFactor", 0.05F, 0, 10000).setToolTip("thirst += fadeFactor per Tick"));
		
		branch.registerElement("minThirstLevelEffected", new IntegerSegment("minThirstLevelEffected", 2, 0, 20).setToolTip("the minimum point thirst is effected"));
		branch.registerElement("maxThirstLevelEffected", new IntegerSegment("maxThirstLevelEffected", 8, 0, 20).setToolTip("the maximum point thirst is effected"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		defaultIntensity = (Float) branch.getValue("defaultIntensity");
		maxIntensity = (Float) branch.getValue("maxIntensity");
		fadeFactor = (Float) branch.getValue("fadeFactor");
		
		minThirstLevelEffected = (Integer) branch.getValue("minThirstLevelEffected");
		maxThirstLevelEffected = (Integer) branch.getValue("maxThirstLevelEffected");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player) {
		VisualPersistent visual = VisualManager.getPersitentVisual(ToughAsNailsAddon.focus);
		if (visual != null) {
			float aimedSaturation = defaultIntensity;
			if (player != null) {
				if (((IThirst) player.getCapability(TANCapabilities.THIRST, null)).getThirst() <= maxThirstLevelEffected) {
					float leftFoodInSpan = ((IThirst) player.getCapability(TANCapabilities.THIRST, null)).getThirst() - minThirstLevelEffected;
					float spanLength = maxThirstLevelEffected - minThirstLevelEffected;
					aimedSaturation = (1 - (leftFoodInSpan / spanLength)) * maxIntensity;
				}
			}
			
			if (visual.getIntensity(1.0F) < aimedSaturation)
				visual.setIntensity(Math.min(visual.getIntensity(1.0F) + fadeFactor, aimedSaturation));
			else if (visual.getIntensity(1.0F) > aimedSaturation)
				visual.setIntensity(Math.max(visual.getIntensity(1.0F) - fadeFactor, aimedSaturation));
		}
	}
	
}

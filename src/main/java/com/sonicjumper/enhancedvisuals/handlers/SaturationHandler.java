package com.sonicjumper.enhancedvisuals.handlers;

import javax.annotation.Nullable;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.creativemd.igcm.api.segments.IntegerSegment;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;
import team.creative.enhancedvisuals.client.VisualManager;

public class SaturationHandler extends VisualHandler {
	
	public SaturationHandler() {
		super("saturation", "saturation depending on hunger");
	}
	
	public float defaultSaturation = 1F;
	public float minSaturation = 0F;
	public float fadeFactor = 0.0005F;
	
	public int minFoodLevelEffected = 2;
	public int maxFoodLevelEffected = 8;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		defaultSaturation = config.getFloat("defaultSaturation", name, defaultSaturation, 0, 10000, "the default/max saturation");
		minSaturation = config.getFloat("minSaturation", name, minSaturation, 0, 10000, "lowest saturation");
		fadeFactor = config.getFloat("fadeFactor", name, fadeFactor, 0, 10000, "saturation += fadeFactor per Tick");
		
		minFoodLevelEffected = config.getInt("minFoodLevelEffected", name, minFoodLevelEffected, 0, 20, "the minimum point saturation is effected");
		maxFoodLevelEffected = config.getInt("maxFoodLevelEffected", name, maxFoodLevelEffected, 0, 20, "the maximum point saturation is effected");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("defaultSaturation", new FloatSegment("defaultSaturation", 1F, 0, 10000).setToolTip("the default/max saturation"));
		branch.registerElement("minSaturation", new FloatSegment("minSaturation", 0F, 0, 10000).setToolTip("lowest saturation"));
		branch.registerElement("fadeFactor", new FloatSegment("fadeFactor", 0.0005F, 0, 10000).setToolTip("saturation += fadeFactor per Tick"));
		
		branch.registerElement("minFoodLevelEffected", new IntegerSegment("minFoodLevelEffected", 2, 0, 20).setToolTip("the minimum point saturation is effected"));
		branch.registerElement("maxFoodLevelEffected", new IntegerSegment("maxFoodLevelEffected", 8, 0, 20).setToolTip("the maximum point saturation is effected"));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		defaultSaturation = (Float) branch.getValue("defaultSaturation");
		minSaturation = (Float) branch.getValue("minSaturation");
		fadeFactor = (Float) branch.getValue("fadeFactor");
		
		minFoodLevelEffected = (Integer) branch.getValue("minFoodLevelEffected");
		maxFoodLevelEffected = (Integer) branch.getValue("maxFoodLevelEffected");
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player) {
		VisualPersistent visual = VisualManager.getPersitentVisual(VisualType.desaturate);
		if (visual != null) {
			float aimedSaturation = defaultSaturation;
			
			if (player != null) {
				if (player.getFoodStats().getFoodLevel() <= maxFoodLevelEffected) {
					float leftFoodInSpan = player.getFoodStats().getFoodLevel() - minFoodLevelEffected;
					float spanLength = maxFoodLevelEffected - minFoodLevelEffected;
					aimedSaturation = Math.max(minSaturation, (leftFoodInSpan / spanLength) * defaultSaturation);
				}
			}
			
			if (visual.getIntensity(1.0F) < aimedSaturation)
				visual.setIntensity(Math.min(visual.getIntensity(1.0F) + fadeFactor, aimedSaturation));
			else if (visual.getIntensity(1.0F) > aimedSaturation)
				visual.setIntensity(Math.max(visual.getIntensity(1.0F) - fadeFactor, aimedSaturation));
		}
	}
	
}

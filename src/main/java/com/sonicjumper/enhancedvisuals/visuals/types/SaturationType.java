package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class SaturationType extends VisualType {

	public SaturationType() {
		super(Visual.VisualCatagory.Shader, "desaturate", "hunger depending saturation");
	}
	
	public float defaultSaturation = 1F;
	public float minSaturation = 0F;
	public float fadeFactor = 0.0005F;
	public int maxFoodLevelEffected = 8;
	public int minFoodLevelEffected = 2;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		defaultSaturation = config.getFloat("defaultSaturation", getName(), 1F, 0, 10000, "the default/max saturation");
		minSaturation = config.getFloat("minSaturation", getName(), 0F, 0, 10000, "lowest saturation");
		fadeFactor = config.getFloat("fadeFactor", getName(), 0.0005F, 0, 10000, "saturation += fadeFactor per Tick");
		maxFoodLevelEffected = config.getInt("maxFoodLevelEffected", getName(), 8, 0, 20, "the maximum point saturation is effected");
		minFoodLevelEffected = config.getInt("minFoodLevelEffected", getName(), 2, 0, 20, "the minimum point saturation is effected");
	}
}

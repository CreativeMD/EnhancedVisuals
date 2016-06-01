package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class SlenderType extends VisualType{

	public SlenderType() {
		super(Visual.VisualCatagory.Animation, "slender", "slenderman effect if you are near an enderman");
	}
	
	public float defaultIntensity = 0F;
	public float slenderDistanceFactor = 0.25F;
	public float maxIntensity = 0.6F;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		defaultIntensity = config.getFloat("defaultIntensity", getName(), 0F, 0, 1, "the default intensity");
		slenderDistanceFactor = config.getFloat("slenderDistanceFactor", getName(), 0.25F, 0, 10000, "intensity = distance * slenderDistanceFactor");
		maxIntensity = config.getFloat("maxIntensity", getName(), 0.6F, 0, 1, "maximum intensity");
	}

}

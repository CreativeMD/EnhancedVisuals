package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class DrownType extends VisualType {

	public DrownType() {
		super(Visual.VisualCatagory.Splat, "water", "drowning splashes");
	}
	
	public int minSplashes = 4;
	public int maxSplashes = 8;
	
	public int minDuration = 10;
	public int maxDuration = 15;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		minSplashes = config.getInt("minSplashes", getName(), 4, 0, 10000, "min splahes");
		maxSplashes = config.getInt("maxSplashes", getName(), 8, 0, 10000, "max splahes");
		
		maxDuration = config.getInt("maxDuration", getName(), 10, 1, 10000, "max duration of one splash");
		minDuration = config.getInt("minDuration", getName(), 15, 1, 10000, "min duration of one splash");
	}

}

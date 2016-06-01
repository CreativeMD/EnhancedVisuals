package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class FireType extends VisualType {

	public FireType() {
		super(Visual.VisualCatagory.Splat, "fire", "burning/ walking on fire", true);
	}
	
	public int splashes = 1;
	
	public int minDuration = 100;
	public int maxDuration = 1000;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		splashes = config.getInt("splashes", getName(), 1, 0, 10000, "splashes per tick");
		
		maxDuration = config.getInt("maxDuration", getName(), 1000, 1, 10000, "max duration of one particle");
		minDuration = config.getInt("minDuration", getName(), 100, 1, 10000, "min duration of one particle");
	}

}

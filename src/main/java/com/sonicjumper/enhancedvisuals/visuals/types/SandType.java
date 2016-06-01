package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class SandType extends VisualType {

	public SandType() {
		super(Visual.VisualCatagory.Splat, "sand", "walking on sand", true);
	}
	
	public float defaultmodifier = 0.5F;
	public float sprintingmodifier = 1.5F;
	
	public int maxDuration = 100;
	public int minDuration = 100;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		defaultmodifier = config.getFloat("defaultmodifier", getName(), 0.5F, 0, 10000, "modifier: splashes per tick = (int) modifier * Math.random()");
		sprintingmodifier = config.getFloat("sprintingmodifier", getName(), 1.5F, 0, 10000, "sprinting -> increased modifier");
		
		maxDuration = config.getInt("maxDuration", getName(), 100, 1, 10000, "max duration of one splash");
		minDuration = config.getInt("minDuration", getName(), 100, 1, 10000, "min duration of one splash");
	}
}

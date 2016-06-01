package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual.VisualCatagory;

import net.minecraftforge.common.config.Configuration;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

public class DustType extends VisualType {

	public DustType() {
		super(Visual.VisualCatagory.Splat, "dust", "explosion dust", true);
	}
	
	public float multiplier = 10F;
	
	public int maxDuration = 10000;
	public int minDuration = 1000;
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		multiplier = config.getFloat("multiplier", getName(), 10F, 0, 10000, "damage * multiplier = number of splats");
		
		maxDuration = config.getInt("maxDuration", getName(), 10000, 1, 100000, "max duration of one particle");
		minDuration = config.getInt("minDuration", getName(), 1000, 1, 100000, "min duration of one particle");
	}
}

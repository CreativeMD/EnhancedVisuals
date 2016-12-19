package com.sonicjumper.enhancedvisuals.visuals.types;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraftforge.common.config.Configuration;

public class BlurType extends VisualType {
	
	public BlurType()
	{
		super(Visual.VisualCatagory.shader, "blur", "heart beat & splash blur effect");
	}
		
	public float splashMinDuration = 30.0F;
	public float splashAdditionalDuration = 30.0F;
	public float splashMinIntensity = 10.0F;
	public float splashAdditionalIntensity = 5.0F;
	
	public float heartBeatIntensity = 50.0F;
	public float heartBeatDuration = 10.0F;
	
	
	@Override
	public void loadConfig(Configuration config) {
		super.loadConfig(config);
		splashMinDuration = config.getFloat("splashMinDuration", getName(), 30F, 0, 10000, "minimum splash duration");
		splashAdditionalDuration = config.getFloat("splashAdditionalDuration", getName(), 30F, 0, 10000, "maximum additional splash duration");
		splashMinIntensity = config.getFloat("splashMinIntensity", getName(), 10F, 0, 10000, "minimum splash intensity");
		splashAdditionalIntensity = config.getFloat("splashAdditionalIntensity", getName(), 5F, 0, 10000, "maximum additional splash intensity");
		
		heartBeatIntensity = config.getFloat("heartBeatIntensity", getName(), 50F, 0, 10000, "heartbeat intensity");
		heartBeatDuration = config.getFloat("heartBeatDuration", getName(), 10F, 0, 10000, "heartbeat duration");
		
		
	}
}

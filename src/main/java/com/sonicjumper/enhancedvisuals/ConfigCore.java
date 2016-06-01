package com.sonicjumper.enhancedvisuals;

import java.io.File;

import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import net.minecraftforge.common.config.Configuration;

public class ConfigCore {
	
	private Configuration config;
	
	public static String defaultThemePack = "DefaultTheme";
	
	public static float waterSubstractFactor = 2.5F;
	
	public static int maxHearts = 6;
	public static float minHeartBeatLength = 15;
	public static float heartBeatHeartFactor = 10;
	
	public static int maxExplosionTime = 1000;
	public static float explosionTimeModifier = 20F;
	public static float minExplosionVolume = 0F;
	public static float explosionVolumeModifier = 10F;
	public static float maxBeepVolume = 0.8F;
	public static float maxBlur = 100;
	public static float blurTimeFactor = 2.5F;

	public ConfigCore(File configFile)
	{
		this.config = new Configuration(configFile);
	}

	public void loadConfig()
	{
		this.config.load();
		
		waterSubstractFactor = config.getFloat("waterSubstractFactor", "general", 2.5F, 0, 100000, "increased fade out factor in water");
		
		minHeartBeatLength = config.getFloat("minHeartBeatLength", "heartbeat", 10, 0, 100000, "time between heartbeats = player.health * heartBeatHeartFactor + minHeartBeatLength");
		heartBeatHeartFactor = config.getFloat("heartBeatHeartFactor", "heartbeat", 10, 0, 100000, "time between heartbeats = player.health * heartBeatHeartFactor + minHeartBeatLength");
		
		maxExplosionTime = config.getInt("maxExplosionTime", "explosion", maxExplosionTime, 0, 100000, "maximum explosion duration");
		explosionTimeModifier = config.getFloat("explosionTimeModifier", "explosion", explosionTimeModifier, 0, 100000, "time = Math.max(maxExplosionTime, damage*explosionTimeModifier)");
		minExplosionVolume = config.getFloat("minExplosionVolume", "explosion", minExplosionVolume, 0, 100000, "factor of all other sounsd (muting effect)");
		explosionVolumeModifier = config.getFloat("explosionVolumeModifier", "explosion", explosionVolumeModifier, 0, 100000, "volume of beep = damage/ConfigCore.explosionVolumeModifier");
		maxBeepVolume = config.getFloat("maxBeepVolume", "explosion", maxBeepVolume, 0, 100000, "max volume of a beep");
		maxBlur = config.getFloat("maxBlur", "explosion", maxBlur, 0, 100000, "max blur effect");
		blurTimeFactor = config.getFloat("blurTimeFactor", "explosion", blurTimeFactor, 0, 100000, "time of blur = time of muted sounds / blurTimeFactor");
		
		for (int i = 0; i < VisualType.visuals.size(); i++) {
			VisualType.visuals.get(i).loadConfig(config);
		}
		
		this.config.save();
	}
}

package com.sonicjumper.enhancedvisuals.handlers;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.events.SoundMuteHandler;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class ExplosionHandler extends VisualHandler {

	public ExplosionHandler() {
		super("explosion", "");
	}
	
	public static int maxExplosionTime = 1000;
	public static float explosionTimeModifier = 20F;
	public static float minExplosionVolume = 0F;
	public static float explosionVolumeModifier = 10F;
	public static float maxBeepVolume = 0.5F;
	public static float maxBlur = 100;
	public static float blurTimeFactor = 2.5F;
	
	public float dustSplatsMultiplier = 10F;
	
	public int dustMaxDuration = 1000;
	public int dustMinDuration = 500;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		maxExplosionTime = config.getInt("maxExplosionTime", name, maxExplosionTime, 0, 100000, "maximum explosion duration");
		explosionTimeModifier = config.getFloat("explosionTimeModifier", name, explosionTimeModifier, 0, 100000, "time = Math.max(maxExplosionTime, damage*explosionTimeModifier)");
		minExplosionVolume = config.getFloat("minExplosionVolume", name, minExplosionVolume, 0, 100000, "factor of all other sounsd (muting effect)");
		explosionVolumeModifier = config.getFloat("explosionVolumeModifier", name, explosionVolumeModifier, 0, 100000, "volume of beep = damage/ConfigCore.explosionVolumeModifier");
		maxBeepVolume = config.getFloat("maxBeepVolume", name, maxBeepVolume, 0, 100000, "max volume of a beep");
		maxBlur = config.getFloat("maxBlur", name, maxBlur, 0, 100000, "max blur effect");
		blurTimeFactor = config.getFloat("blurTimeFactor", name, blurTimeFactor, 0, 100000, "time of blur = time of muted sounds / blurTimeFactor");
		
		dustSplatsMultiplier = config.getFloat("dustSplatsMultiplier", name, dustSplatsMultiplier, 0, 10000, "damage * multiplier = number of splats");
		
		dustMaxDuration = config.getInt("dustMaxDuration", name, dustMaxDuration, 1, 100000, "max duration of one particle");
		dustMinDuration = config.getInt("dustMinDuration", name, dustMinDuration, 1, 100000, "min duration of one particle");
	}
	
	@Override
	public void onExplosion(EntityPlayer player, double x, double y, double z, double distance)
	{
		if(distance < 5)
		{
			float damage = (float) (1-(distance/5F))*5;
			System.out.println(damage);
			VisualManager.addVisualsWithShading(VisualType.dust, (int) Math.min(40, damage*dustSplatsMultiplier), dustMinDuration, dustMaxDuration);
			
			float volume = Math.max(minExplosionVolume, 1-(damage/explosionVolumeModifier));
			int time = (int) Math.min(maxExplosionTime, damage*explosionTimeModifier);
			
			if(!SoundMuteHandler.isMuting)
				playSound(new ResourceLocation("enhancedvisuals:ringing"), null, (1-volume)*maxBeepVolume);
			SoundMuteHandler.startMuting(time, volume);
			VisualManager.addVisualWithShading(VisualType.blur, maxBlur, (int) (time/blurTimeFactor), (int) (time/blurTimeFactor), Color.WHITE);
		}
	}
	
}

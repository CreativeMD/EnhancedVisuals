package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;

public class VisualFadeOut extends Visual {
	
	public int duration;
	public int lifeTime = 0;
	
	public VisualFadeOut(VisualType type, float intensity, int minDuration, int maxDuration, Color color) {
		super(type, intensity, color);
		this.duration = minDuration + (maxDuration - minDuration > 0 ? rand.nextInt(maxDuration - minDuration) : minDuration);
	}

	public VisualFadeOut(VisualType type, float intensity, int minDuration, int maxDuration) {
		this(type, intensity, minDuration, maxDuration, Color.WHITE);
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		lifeTime++;
	}
	
	@Override
	public float getIntensity(float partialTicks)
	{
		float lifeTime = this.lifeTime + partialTicks;
		if(type.supportsColor())
			return color.getAlpha() / 255F * intensity * (1F - (lifeTime / (float) duration));
		return (float) (intensity * (1 - (lifeTime / (float) duration)));
	}

	@Override
	public boolean hasFinished() {
		return lifeTime >= duration;
	}

	@Override
	public boolean isRandomized() {
		return type.isRandomized();
	}

}

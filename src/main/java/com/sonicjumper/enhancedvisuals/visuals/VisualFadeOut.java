package com.sonicjumper.enhancedvisuals.visuals;

import javax.annotation.Nullable;

import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.entity.player.EntityPlayer;

public class VisualFadeOut extends Visual {
	
	public int duration;
	public int lifeTime = 0;

	public VisualFadeOut(VisualType type, float intensity, int minDuration, int maxDuration) {
		super(type, intensity);
		this.duration = minDuration + rand.nextInt(maxDuration - minDuration);
	}
	
	@Override
	public void onTick(@Nullable EntityPlayer player)
	{
		lifeTime++;
	}
	
	@Override
	public float getIntensity()
	{
		if(type.supportsColor())
			return color.getAlpha() / 255F * intensity * (lifeTime/duration);
		return intensity * (lifeTime/duration);
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

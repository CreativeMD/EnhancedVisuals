package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;

public class ReverseGlowBlur extends GlowBlur {
	private GlowBlur resultBlur;
	
	public ReverseGlowBlur(GlowBlur clone) {
		this(clone.getType(), clone.getMaxTime(), clone.getColor(), clone.resetsOnTick, clone.getScale(), clone.getRadius(), clone.getGlowAmount(), clone);
	}
	
	public ReverseGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, float amount, GlowBlur result) {
		super(type, time, rgba, resets, scale, radius, amount);
		resultBlur = result;
	}
	
	@Override
	public void beingRemoved() {
		Base.instance.manager.addVisualDirect(resultBlur);
	}
	
	@Override
	public float getTranslucencyByTime() {
		return (1 - super.getTranslucencyByTime());
	}
}

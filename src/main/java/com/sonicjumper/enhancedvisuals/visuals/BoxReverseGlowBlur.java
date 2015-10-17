package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;

public class BoxReverseGlowBlur extends BoxGlowBlur {
	private BoxGlowBlur resultBlur;
	
	public BoxReverseGlowBlur(BoxGlowBlur clone) {
		this(clone.getType(), clone.getMaxTime(), clone.getColor(), clone.resetsOnTick, clone.getScale(), clone.getRadius(), clone.getIterations(), clone.getGlowAmount(), clone);
	}
	
	public BoxReverseGlowBlur(VisualType type, int time, Color rgba, boolean resets, float scale, int radius, int iterations, float amount, BoxGlowBlur result) {
		super(type, time, rgba, resets, scale, radius, iterations, amount);
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

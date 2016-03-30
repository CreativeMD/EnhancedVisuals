package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderAnimation;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class Animation extends Visual {
	
	public int animationSpeed;
	public float intensity;
	
	public Animation(VisualType type, int time, Color rgba)
	{
		this(type, time, rgba, 50);
	}
	
	public Animation(VisualType type, int time, Color rgba, int animationSpeed)
	{
		super(type, time, rgba);
		this.animationSpeed = animationSpeed;
	}
	
	@Override
	public float getTranslucencyByTime() {
		return intensity;
	}
	
	@Override
	public RenderVisual getRenderer()
	{
		return Base.instance.renderer.getRendererForClass(RenderAnimation.class);
	}
}

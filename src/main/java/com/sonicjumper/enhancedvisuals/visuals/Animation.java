package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderAnimation;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class Animation extends Visual {
	public Animation(VisualType type, int time, Color rgba)
	{
		super(type, time, rgba);
	}

	public RenderVisual getRenderer()
	{
		return Base.instance.renderer.getRendererForClass(RenderAnimation.class);
	}
}

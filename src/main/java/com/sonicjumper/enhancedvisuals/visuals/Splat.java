package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderSplat;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class Splat extends Visual {
	public Splat(VisualType type, int time, Color rgba) {
		super(type, time, rgba);
	}

	public Splat(VisualType type, int time, Color rgba, float xOffset, float yOffset) {
		super(type, time, rgba, xOffset, yOffset);
	}

	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderSplat.class);
	}
}

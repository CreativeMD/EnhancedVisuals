package com.sonicjumper.enhancedvisuals.visuals;

import java.awt.Color;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderOverlay;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class Overlay extends Visual {
	public Overlay(VisualType type, int time, Color rgba) {
		super(type, time, rgba);
	}

	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderOverlay.class);
	}
}

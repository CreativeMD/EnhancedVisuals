package com.sonicjumper.enhancedvisuals.visuals.types;

import net.minecraftforge.common.config.Configuration;

public abstract class VisualTypeOverlay extends VisualTypeTexture {

	public VisualTypeOverlay(String name, String comment, int animationSpeed) {
		super(VisualCategory.overlay, name, comment, animationSpeed);
	}
	
	public VisualTypeOverlay(String name, String comment) {
		this(name, comment, 0);
	}
	
	@Override
	public boolean isRandomized() {
		return false;
	}

}

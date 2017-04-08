package com.sonicjumper.enhancedvisuals.visuals.types;

import net.minecraftforge.common.config.Configuration;

public class VisualTypeOverlay extends VisualTypeTexture {

	public VisualTypeOverlay(String name, int animationSpeed, boolean isAffectedByWater) {
		super(VisualCategory.overlay, name, animationSpeed, isAffectedByWater);
	}
	
	public VisualTypeOverlay(String name, boolean isAffectedByWater) {
		this(name, 0, isAffectedByWater);
	}
	
	@Override
	public boolean isRandomized() {
		return false;
	}

}

package com.sonicjumper.enhancedvisuals.visuals.types;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;

public class VisualTypeSplat extends VisualTypeTexture {
	
	public VisualTypeSplat(String name, int animationSpeed, boolean isAffectedByWater) {
		super(VisualCategory.splat, name, animationSpeed, isAffectedByWater);
	}

	public VisualTypeSplat(String name, boolean isAffectedByWater) {
		this(name, 0, isAffectedByWater);
	}
	
	public int getSize()
	{
		return (int) (this.dimension.height * scaleFactor);
	}
	
	public float scaleFactor = 1F;

	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		scaleFactor = config.getFloat("scaleFactor", getConfigCat(), scaleFactor, 0, 1000, "");
	}

	@Override
	public boolean isRandomized() {
		return true;
	}
	
}

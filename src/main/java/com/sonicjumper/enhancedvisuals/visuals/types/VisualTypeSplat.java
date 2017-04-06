package com.sonicjumper.enhancedvisuals.visuals.types;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.config.Configuration;

public abstract class VisualTypeSplat extends VisualTypeTexture {
	
	public VisualTypeSplat(String name, String comment, int animationSpeed) {
		super(VisualCategory.splat, name, comment, animationSpeed);
	}

	public VisualTypeSplat(String name, String comment) {
		this(name, comment, 0);
	}
	
	public int getSize()
	{
		return (int) (this.dimension.height * scaleFactor);
	}
	
	public float scaleFactor = 1F;

	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		scaleFactor = config.getFloat("scaleFactor", name, scaleFactor, 0, 1000, "");
	}

	@Override
	public boolean isRandomized() {
		return true;
	}
	
}

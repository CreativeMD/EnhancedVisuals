package com.sonicjumper.enhancedvisuals.visuals.types;

import com.creativemd.igcm.api.ConfigBranch;

import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VisualTypeOverlay extends VisualTypeTexture {
	
	public VisualTypeOverlay(String name, int animationSpeed, boolean isAffectedByWater) {
		super(VisualCategory.overlay, name, animationSpeed, isAffectedByWater);
	}
	
	public VisualTypeOverlay(String name, boolean isAffectedByWater) {
		this(name, 0, isAffectedByWater);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isRandomized() {
		return false;
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		
	}
	
}

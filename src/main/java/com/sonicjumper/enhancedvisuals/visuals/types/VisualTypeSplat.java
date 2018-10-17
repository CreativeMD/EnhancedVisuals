package com.sonicjumper.enhancedvisuals.visuals.types;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.FloatSegment;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VisualTypeSplat extends VisualTypeTexture {
	
	public VisualTypeSplat(String name, int animationSpeed, boolean isAffectedByWater) {
		super(VisualCategory.splat, name, animationSpeed, isAffectedByWater);
	}
	
	public VisualTypeSplat(String name, boolean isAffectedByWater) {
		this(name, 0, isAffectedByWater);
	}
	
	@SideOnly(Side.CLIENT)
	public int getSize() {
		return (int) (this.dimension.height * scaleFactor);
	}
	
	public float scaleFactor = 1F;
	
	@Override
	public void initConfig(Configuration config) {
		super.initConfig(config);
		scaleFactor = config.getFloat("scaleFactor", getConfigCat(), scaleFactor, 0, 1000, "");
	}
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		branch.registerElement("scale", new FloatSegment("Scale-Factor", 1F, 0, 1000));
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		scaleFactor = (Float) branch.getValue("scale");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean isRandomized() {
		return true;
	}
	
}

package com.sonicjumper.enhancedvisuals.visuals;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlurFade;
import com.sonicjumper.enhancedvisuals.render.RenderShaderDesaturate;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class ShaderDesaturate extends Shader {
	
	public static float Saturation = 1;
	
	public ShaderDesaturate(VisualType type, int lifeTime) {
		super(type, lifeTime);
	}
	
	@Override
	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderShaderDesaturate.class);
	}

	public float getMaxBlurSaturation() {
		return Saturation;
	}
	
}

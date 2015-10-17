package com.sonicjumper.enhancedvisuals.visuals;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderShader;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlurFade;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class ShaderBlurFade extends Shader {
	private float blurRadius;
	
	public ShaderBlurFade(VisualType type, int lifeTime, float maxBlur) {
		super(type, lifeTime);
		this.blurRadius = maxBlur;
	}
	
	@Override
	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderShaderBlurFade.class);
	}

	public float getMaxBlurRadius() {
		return blurRadius;
	}
}

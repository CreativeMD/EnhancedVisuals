package com.sonicjumper.enhancedvisuals.visuals;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlob2;
import com.sonicjumper.enhancedvisuals.render.RenderShaderBlurFade;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;

public class ShaderBlob2 extends Shader {
	
	private float radius;
	
	public ShaderBlob2(VisualType type, int lifeTime, float radius) {
		super(type, lifeTime);
		this.radius = radius;
	}
	
	@Override
	public RenderVisual getRenderer() {
		return Base.instance.renderer.getRendererForClass(RenderShaderBlob2.class);
	}

	public float getRadius() {
		return radius;
	}

}

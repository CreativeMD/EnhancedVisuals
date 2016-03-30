package com.sonicjumper.enhancedvisuals.render;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.shaders.ShaderHelper;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

public abstract class RenderShader extends RenderVisual {
	protected ShaderHelper shaderHelper;
	
	public RenderShader() {
		shaderHelper = Base.instance.shaderHelper;
	}
	
	@Override
	public void renderVisual(Visual v, float partialTicks) {
		if(!shaderHelper.isShaderActive(v.getType().getName())) {
			shaderHelper.loadShader(v.getType().getName(), v.getResource());
		}
		updateUniforms(v);
		//if(v.shouldRemove()) {
		//	shaderHelper.removeShader(v.getType().getName());
		//}
	}

	/**
	 * Use this to set uniforms inside the shader file. Note: Being able to customize this method is the only reason to extend the Shader.class
	 * and create a new RenderShader.class file, opposed to just using the VisualType.defaultShader and the normal Shader.class
	 * @param s
	 */
	protected abstract void updateUniforms(Visual v);
}

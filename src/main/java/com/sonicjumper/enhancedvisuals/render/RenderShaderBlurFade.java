package com.sonicjumper.enhancedvisuals.render;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.shaders.ShaderGroupCustom;
import com.sonicjumper.enhancedvisuals.visuals.ShaderBlurFade;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

public class RenderShaderBlurFade extends RenderShader {
	private static String RADIUS_UNIFORM = "Radius";
	
	public static float lastBlurRadius = 0F;
	
	@Override
	protected void updateUniforms(Visual v) {
		ShaderBlurFade sbf = (ShaderBlurFade) v;
		//if(sbf.getMaxBlurRadius() > 0)
		//{
			// Update blur radius based on ticks remaining in the Visual
			ShaderGroupCustom group = shaderHelper.getShaderGroup(v.getType().getName());
			// Remember that there are two shaders in this group, the vertical blur and horizontal blur, and the code must change both of their radii
			if(group != null)
			{
				for(Shader mcShader : group.getShaders()) {
					ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform(RADIUS_UNIFORM);
					
					if (shaderuniform != null) {
						Float currentBlurRadius = (float) Math.floor((v.getTranslucencyByTime() * (sbf.getMaxBlurRadius() - 1.0F)) + 1.0F);
			        	shaderuniform.set(currentBlurRadius);
			        	lastBlurRadius = currentBlurRadius;
			        }// else {
			        //	Base.log.warn("The Shader Uniform " + RADIUS_UNIFORM + " does not exist");
			        //}
				}
			}
		//}
	}
	
	public static void resetBlur()
	{
		float currentBlurRadius = 0F;
		ShaderGroupCustom group = Base.instance.shaderHelper.getShaderGroup("blur");
		// Remember that there are two shaders in this group, the vertical blur and horizontal blur, and the code must change both of their radii
		if(group != null)
		{
			for(Shader mcShader : group.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform(RADIUS_UNIFORM);
				
				if (shaderuniform != null) {
		        	shaderuniform.set(currentBlurRadius);
		        	lastBlurRadius = currentBlurRadius;
		        }// else {
		        //	Base.log.warn("The Shader Uniform " + RADIUS_UNIFORM + " does not exist");
		        //}
			}
		}
	}
}

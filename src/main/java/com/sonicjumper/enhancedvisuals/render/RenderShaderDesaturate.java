package com.sonicjumper.enhancedvisuals.render;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.client.util.JsonException;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.shaders.ShaderGroupCustom;
import com.sonicjumper.enhancedvisuals.visuals.ShaderBlurFade;
import com.sonicjumper.enhancedvisuals.visuals.ShaderDesaturate;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

public class RenderShaderDesaturate extends RenderShader {
	private static String SATURATION_UNIFORM = "Saturation";
	
	public static float lastSaturation = 1F;
	
	@Override
	protected void updateUniforms(Visual v) {
		ShaderDesaturate sbf = (ShaderDesaturate) v;
		//if(sbf.getMaxBlurRadius() > 0)
		//{
			// Update blur radius based on ticks remaining in the Visual
			ShaderGroupCustom group = shaderHelper.getShaderGroup(v.getType().getName());
			// Remember that there are two shaders in this group, the vertical blur and horizontal blur, and the code must change both of their radii
			if(group != null)
			{
				for(Shader mcShader : group.getShaders()) {
					ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform(SATURATION_UNIFORM);
					
					if (shaderuniform != null) {
						Float currentSaturation = 1-(v.getTranslucencyByTime() * (1-sbf.getMaxBlurSaturation()));
			        	shaderuniform.set(currentSaturation);
			        	lastSaturation = currentSaturation;
			        	//System.out.println(lastSaturation);
			        } else {
			        	//Base.log.warn("The Shader Uniform " + SATURATION_UNIFORM + " does not exist");
			        }
				}
			}
		//}
	}
	
	public static void resetSaturation()
	{
		float currentSaturation = 1F;
		ShaderGroupCustom group = Base.instance.shaderHelper.getShaderGroup("desaturate");
		// Remember that there are two shaders in this group, the vertical blur and horizontal blur, and the code must change both of their radii
		if(group != null)
		{
			for(Shader mcShader : group.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform(SATURATION_UNIFORM);
				
				if (shaderuniform != null) {
		        	shaderuniform.set(currentSaturation);
		        	lastSaturation = currentSaturation;
		        } else {
		        	//Base.log.warn("The Shader Uniform " + SATURATION_UNIFORM + " does not exist");
		        }
			}
		}
	}
}

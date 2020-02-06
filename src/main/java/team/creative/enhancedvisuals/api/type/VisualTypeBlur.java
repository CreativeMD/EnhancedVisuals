package team.creative.enhancedvisuals.api.type;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class VisualTypeBlur extends VisualTypeShader {
	
	public static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blur.json");
	
	public VisualTypeBlur(String name) {
		super(name, BLUR_SHADER);
	}
	
	@Override
	@OnlyIn(value = Dist.CLIENT)
	public void changeProperties(float intensity) {
		for (Shader mcShader : shaderGroup.getShaders()) {
			ShaderUniform shaderuniform = mcShader.getShaderManager().func_216539_a("Radius");
			
			if (shaderuniform != null)
				shaderuniform.set((float) Math.floor(intensity));
		}
	}
	
}

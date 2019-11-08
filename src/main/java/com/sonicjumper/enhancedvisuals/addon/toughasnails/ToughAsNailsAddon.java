package com.sonicjumper.enhancedvisuals.addon.toughasnails;

import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.api.type.VisualTypeShader;

public class ToughAsNailsAddon {
	
	public static VisualType focus = new VisualTypeShader("focus", new ResourceLocation("shaders/post/blobs2.json"), false) {
		
		@Override
		public VisualPersistent createPersitentVisual() {
			return new VisualPersistent(this, 0);
		}
		
		@Override
		public void changeProperties(float intensity) {
			for (Shader mcShader : shaderGroup.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Radius");
				
				if (shaderuniform != null) {
					shaderuniform.set(intensity);
				}
			}
		}
		
		@Override
		public boolean needsToBeRendered(float intensity) {
			return intensity > 0;
		}
	};
	
	public static VisualType freeze = new VisualTypeOverlay("freeze", false) {
		
		@Override
		public VisualPersistent createPersitentVisual() {
			return new VisualPersistent(this, 0);
		}
		
	};
	
	public static VisualType heat = new VisualTypeOverlay("heat", false) {
		
		@Override
		public VisualPersistent createPersitentVisual() {
			return new VisualPersistent(this, 0);
		}
		
	};
	
	public static ThirstHandler thirst = new ThirstHandler();
	public static TemperatureHandler temperature = new TemperatureHandler();
	
	public static void load() {
		
	}
	
}

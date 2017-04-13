package com.sonicjumper.enhancedvisuals.visuals.types;

import java.util.ArrayList;
import java.util.HashMap;

import com.sonicjumper.enhancedvisuals.addon.toughasnails.ToughAsNailsAddon;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public abstract class VisualType {
	
	public static VisualType splatter = new VisualTypeSplat("splatter", true);
	public static VisualType impact = new VisualTypeSplat("impact", true);
	public static VisualType slash = new VisualTypeSplat("slash", true);
	public static VisualType pierce = new VisualTypeSplat("pierce", true);
	public static VisualType dust = new VisualTypeSplat("dust", true);
	public static VisualType fire = new VisualTypeSplat("fire", true);
	public static VisualType sand = new VisualTypeSplat("sand", true);
	public static VisualType waterS = new VisualTypeSplat("water", false);
	
	public static VisualType lowhealth = new VisualTypeOverlay("lowhealth", false);
	public static VisualType damaged = new VisualTypeOverlay("damaged", false);
	public static VisualType potion = new VisualTypeOverlay("potion", false);
	
	public static VisualType slender = new VisualTypeOverlay("slender", 50, false){
		
		@Override
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 0);
		}
		
	};
	
	public static VisualType blur = new VisualTypeShader("blur", new ResourceLocation("shaders/post/blur.json"), false){
		
		@Override
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 0);
		}
		
		@Override
		public void changeProperties(float intensity) {
			for(Shader mcShader : shaderGroup.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Radius");
				
				if (shaderuniform != null) {
		        	shaderuniform.set((float) Math.floor(intensity));
		        }
			}
		}
		
		@Override
		public boolean needsToBeRendered(float intensity) {
			return intensity > 0;
		}
		
	};
	public static VisualType desaturate = new VisualTypeShader("desaturate", new ResourceLocation("shaders/post/desaturate.json"), false){
		
		@Override
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 1);
		}
		
		@Override
		public void changeProperties(float intensity) {
			for(Shader mcShader : shaderGroup.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Saturation");
				
				if (shaderuniform != null) {
		        	shaderuniform.set(intensity);
		        }
			}
		}
		
		@Override
		public boolean needsToBeRendered(float intensity) {
			return intensity != 1;
		}
		
	};
	
	public static void onLoad()
	{
		if(Loader.isModLoaded("toughasnails"))
			ToughAsNailsAddon.load();
	}
	
	public String name;
	
	public final boolean isAffectedByWater;
	public final VisualCategory category;
	
	protected boolean enabled = true;
	public float alpha = 1;
	
	public VisualType(VisualCategory category, String name, boolean isAffectedByWater) {
		this.category = category;
		this.name = name;
		this.isAffectedByWater = isAffectedByWater;
		category.types.add(this);
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	protected String getConfigCat()
	{
		return name + "-type";
	}
	
	public void initConfig(Configuration config)
	{
		enabled = config.getBoolean("enabled", getConfigCat(), true, "If the effect is enabled!");
		alpha = config.getFloat("alpha", getConfigCat(), alpha, 0, 1, "");
	}
	
	public abstract void loadTextures(IResourceManager manager);
	
	public abstract int getVariantAmount();
	
	public abstract boolean isRandomized();
	
	public int getSize()
	{
		return -1;
	}

	public abstract boolean supportsColor();
	
	public abstract void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity);
	
	public void onResize(Framebuffer buffer) {}
	
	public VisualPersistent createPersitentVisual()
	{
		return null;
	}
	
	public abstract boolean needsToBeRendered(float intensity);
}

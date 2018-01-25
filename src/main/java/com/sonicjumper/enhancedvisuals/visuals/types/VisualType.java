package com.sonicjumper.enhancedvisuals.visuals.types;

import java.util.ArrayList;
import java.util.HashMap;

import com.creativemd.igcm.api.ConfigBranch;
import com.creativemd.igcm.api.segments.BooleanSegment;
import com.creativemd.igcm.api.segments.FloatSegment;
import com.sonicjumper.enhancedvisuals.addon.toughasnails.ToughAsNailsAddon;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.shader.ShaderUniform;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		@SideOnly(Side.CLIENT)
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 0);
		}
		
	};
	
	public static VisualType blur = new VisualTypeShader("blur", new ResourceLocation("shaders/post/blur.json"), false){
		
		@Override
		@SideOnly(Side.CLIENT)
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 0);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void changeProperties(float intensity) {
			for(Shader mcShader : shaderGroup.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Radius");
				
				if (shaderuniform != null) {
		        	shaderuniform.set((float) Math.floor(intensity));
		        }
			}
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean needsToBeRendered(float intensity) {
			return Math.floor(intensity) > 0;
		}
		
	};
	public static VisualType desaturate = new VisualTypeShader("desaturate", new ResourceLocation("shaders/post/desaturate.json"), false){
		
		@Override
		@SideOnly(Side.CLIENT)
		public VisualPersistent createPersitentVisual()
		{
			return new VisualPersistent(this, 1);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public void changeProperties(float intensity) {
			for(Shader mcShader : shaderGroup.getShaders()) {
				ShaderUniform shaderuniform = mcShader.getShaderManager().getShaderUniform("Saturation");
				
				if (shaderuniform != null) {
		        	shaderuniform.set(intensity);
		        }
			}
		}
		
		@Override
		@SideOnly(Side.CLIENT)
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
	
	public String getConfigCat()
	{
		return name + "-type";
	}
	
	public void initConfig(Configuration config)
	{
		enabled = config.getBoolean("enabled", getConfigCat(), true, "If the effect is enabled!");
		alpha = config.getFloat("alpha", getConfigCat(), alpha, 0, 1, "");
	}
	
	@Method(modid = "igcm")
	public ConfigBranch getConfigBranch()
	{
		return new ConfigBranch(getConfigCat(), ItemStack.EMPTY) {
			
			@Override
			public void saveExtra(NBTTagCompound nbt) {
				
			}
			
			@Override
			public void loadExtra(NBTTagCompound nbt) {
				
			}
			
			@Override
			public boolean requiresSynchronization() {
				return true;
			}
			
			@Override
			public void onRecieveFrom(Side side) {
				alpha = (Float) getValue("alpha");
				enabled = (Boolean) getValue("enabled");
				receiveConfigElements(this);
			}
			
			@Override
			public void createChildren() {
				registerElement("enabled", new BooleanSegment("Enabled", true).setToolTip("If the effect is enabled!"));
				registerElement("alpha", new FloatSegment("Alpha", (float) 1, 0, 1));
				registerConfigElements(this);
			}
		};
	}
	
	@Method(modid = "igcm")
	public abstract void registerConfigElements(ConfigBranch branch);
	
	@Method(modid = "igcm")
	public abstract void receiveConfigElements(ConfigBranch branch);
	
	@SideOnly(Side.CLIENT)
	public abstract void loadTextures(IResourceManager manager);
	
	@SideOnly(Side.CLIENT)
	public abstract int getVariantAmount();
	
	@SideOnly(Side.CLIENT)
	public abstract boolean isRandomized();
	
	@SideOnly(Side.CLIENT)
	public int getSize()
	{
		return -1;
	}

	@SideOnly(Side.CLIENT)
	public abstract boolean supportsColor();
	
	@SideOnly(Side.CLIENT)
	public abstract void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity);
	
	@SideOnly(Side.CLIENT)
	public void onResize(Framebuffer buffer) {}
	
	@SideOnly(Side.CLIENT)
	public VisualPersistent createPersitentVisual()
	{
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public abstract boolean needsToBeRendered(float intensity);
}

package com.sonicjumper.enhancedvisuals.visuals.types;

import java.util.ArrayList;
import java.util.HashMap;

import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.common.config.Configuration;

public abstract class VisualType {
	
	public String name;
	public String comment;
	
	public final VisualCategory category;
	
	protected boolean enabled = true;
	public float alpha = 1;
	
	public VisualType(VisualCategory category, String name, String comment) {
		this.category = category;
		this.name = name;
		this.comment = comment;
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public void initConfig(Configuration config)
	{
		enabled = config.getBoolean("enabled", name, true, "If the effect is enabled!");
		alpha = config.getFloat("alpha", name, alpha, 0, 1, "");
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
}

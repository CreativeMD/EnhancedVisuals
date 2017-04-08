package com.sonicjumper.enhancedvisuals.visuals.types;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.sonicjumper.enhancedvisuals.utils.EnhancedShaderGroup;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public abstract class VisualTypeShader extends VisualType {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public ResourceLocation location;
	
	public VisualTypeShader(String name, ResourceLocation location, boolean isAffectedByWater) {
		super(VisualCategory.shader, name, isAffectedByWater);
		this.location = location;
	}
	
	public EnhancedShaderGroup shaderGroup = null;

	@Override
	public void loadTextures(IResourceManager manager) {
		if(ShaderLinkHelper.getStaticShaderLinkHelper() != null)
		{
			if(shaderGroup != null)
				shaderGroup.deleteShaderGroup();
			
			try {
				shaderGroup = new EnhancedShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), location);
				shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
			} catch (JsonSyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public int getVariantAmount() {
		return 0;
	}

	@Override
	public boolean isRandomized() {
		return false;
	}

	@Override
	public boolean supportsColor() {
		return false;
	}
	
	@Override
	public void onResize(Framebuffer buffer)
	{
		if(shaderGroup != null)
			shaderGroup.createBindFramebuffers(buffer.framebufferWidth, buffer.framebufferHeight);
	}
	
	public abstract void changeProperties(float intensity);
	
	@Override
	public void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity) {
		if(shaderGroup == null)
			if(ShaderLinkHelper.getStaticShaderLinkHelper() != null)
			{
				try {
					shaderGroup = new EnhancedShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), location);
					shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
				} catch (JsonSyntaxException | IOException e) {
					e.printStackTrace();
				}
			}
		
		if(shaderGroup != null)
		{
			changeProperties(intensity);
			shaderGroup.loadShaderGroup(partialTicks);
		}
	}

}

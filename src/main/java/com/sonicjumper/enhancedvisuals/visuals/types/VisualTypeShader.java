package com.sonicjumper.enhancedvisuals.visuals.types;

import java.io.IOException;

import com.creativemd.igcm.api.ConfigBranch;
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
import net.minecraftforge.fml.common.Optional.Method;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class VisualTypeShader extends VisualType {
	
	public ResourceLocation location;
	
	public VisualTypeShader(String name, ResourceLocation location, boolean isAffectedByWater) {
		super(VisualCategory.shader, name, isAffectedByWater);
		this.location = location;
	}
	
	public EnhancedShaderGroup shaderGroup = null;
	
	@Override
	@Method(modid = "igcm")
	public void registerConfigElements(ConfigBranch branch) {
		
	}
	
	@Override
	@Method(modid = "igcm")
	public void receiveConfigElements(ConfigBranch branch) {
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void loadTextures(IResourceManager manager) {
		if(ShaderLinkHelper.getStaticShaderLinkHelper() != null)
		{
			Minecraft mc = Minecraft.getMinecraft();
			if(shaderGroup != null)
				shaderGroup.deleteShaderGroup();
			
			try {
				shaderGroup = new EnhancedShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), location);
				shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			} catch (JsonSyntaxException | IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getVariantAmount() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isRandomized() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean supportsColor() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onResize(Framebuffer buffer)
	{
		if(shaderGroup != null)
			shaderGroup.createBindFramebuffers(buffer.framebufferWidth, buffer.framebufferHeight);
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void changeProperties(float intensity);
	
	@Override
	@SideOnly(Side.CLIENT)
	public void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity) {
		if(shaderGroup == null)
			if(ShaderLinkHelper.getStaticShaderLinkHelper() != null)
			{
				Minecraft mc = Minecraft.getMinecraft();
				try {
					shaderGroup = new EnhancedShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), location);
					shaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
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

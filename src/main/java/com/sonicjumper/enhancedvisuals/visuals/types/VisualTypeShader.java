package com.sonicjumper.enhancedvisuals.visuals.types;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

public class VisualTypeShader extends VisualType {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public ResourceLocation location;
	
	public VisualTypeShader(String name, String comment, ResourceLocation location) {
		super(VisualCategory.shader, name, comment);
		this.location = location;
	}
	
	public ShaderGroup shaderGroup = null;

	@Override
	public void initConfig(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadTextures(IResourceManager manager) {
		if(shaderGroup != null)
			shaderGroup.deleteShaderGroup();
		
		try {
			shaderGroup = new ShaderGroup(mc.getTextureManager(), manager, mc.getFramebuffer(), location);
			shaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
		} catch (JsonSyntaxException | IOException e) {
			e.printStackTrace();
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
		shaderGroup.createBindFramebuffers(buffer.framebufferWidth, buffer.framebufferHeight);
	}
	
	@Override
	public void render(Visual visual, TextureManager manager, ScaledResolution resolution, float partialTicks, float intensity) {
		shaderGroup.loadShaderGroup(partialTicks);
	}

}

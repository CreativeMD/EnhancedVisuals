package team.creative.enhancedvisuals.api.type;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.client.render.EnhancedShaderGroup;

public abstract class VisualTypeShader extends VisualType {
	
	public ResourceLocation location;
	
	public VisualTypeShader(String name, ResourceLocation location) {
		super(name, VisualCategory.shader);
		this.location = location;
	}
	
	@SideOnly(Side.CLIENT)
	public EnhancedShaderGroup shaderGroup;
	
	@Override
	@SideOnly(Side.CLIENT)
	public void loadResources(IResourceManager manager) {
		if (ShaderLinkHelper.getStaticShaderLinkHelper() != null) {
			Minecraft mc = Minecraft.getMinecraft();
			if (shaderGroup != null)
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
	public boolean supportsColor() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void resize(Framebuffer buffer) {
		if (shaderGroup != null)
			shaderGroup.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
	}
	
	@Override
	public void render(Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		if (shaderGroup == null)
			loadResources(Minecraft.getMinecraft().getResourceManager());
		if (shaderGroup != null) {
			changeProperties(visual.opacity);
			shaderGroup.render(partialTicks);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public abstract void changeProperties(float intensity);
	
}

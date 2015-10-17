package com.sonicjumper.enhancedvisuals.shaders;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

import com.google.gson.JsonSyntaxException;
import com.sonicjumper.enhancedvisuals.Base;

public class ShaderHelper {
	private Minecraft mc;
	private IResourceManager resourceManager;
	private ShaderGroupCustom theShaderGroup;
	
	private String shaderName;
	private boolean useShader;
	
	public ShaderHelper(Minecraft minecraft, IResourceManager resourceManager) {
		this.mc = minecraft;
		this.resourceManager = resourceManager;
	}
	
	public boolean isShaderActive(String name) {
		return name.equals(shaderName);
	}
	
    public void loadShader(String name, ResourceLocation shadersPostLocation) {
    	if(this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }
    	
        try {
            this.theShaderGroup = new ShaderGroupCustom(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), shadersPostLocation);
            this.theShaderGroup.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            this.useShader = true;
            this.shaderName = name;
        } catch (IOException ioexception) {
            Base.log.warn("Failed to load shader: " + shadersPostLocation, ioexception);
            this.useShader = false;
            this.shaderName = "";
        } catch (JsonSyntaxException jsonsyntaxexception) {
            Base.log.warn("Failed to load shader: " + shadersPostLocation, jsonsyntaxexception);
            this.useShader = false;
            this.shaderName = "";
        }
    }

	public void drawShaders(float partialTicks) {
		
		/*mc.getRenderManager().setRenderOutlines(false);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.depthMask(false);*/
        
		if(Base.instance.shaderHelper.getShaderGroup() != null && this.useShader) {
			GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            
            GlStateManager.loadIdentity();
			Base.instance.shaderHelper.getShaderGroup().loadShaderGroup(partialTicks);
            GlStateManager.popMatrix();
            
		}
		
		 //GlStateManager.depthMask(true);
         this.mc.getFramebuffer().bindFramebuffer(false);
         //GlStateManager.enableFog();
         /*GlStateManager.depthFunc(515);
         GlStateManager.enableDepth();
         GlStateManager.enableAlpha();
         GlStateManager.enableRescaleNormal();*/
	}

	public ShaderGroupCustom getShaderGroup() {
		return theShaderGroup;
	}

	public void removeShader(String name) {
		this.useShader = false;
		this.theShaderGroup = null;
	}
}

package com.sonicjumper.enhancedvisuals.shaders;

import java.io.IOException;
import java.util.ArrayList;

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
	private ArrayList<ShaderGroupCustom> groups = new ArrayList<>();
	
	private ArrayList<String> shaders = new ArrayList<>();
	private boolean useShader;
	
	public ShaderHelper(Minecraft minecraft, IResourceManager resourceManager) {
		this.mc = minecraft;
		this.resourceManager = resourceManager;
	}
	
	public boolean isShaderActive(String name) {
		return shaders.contains(name);
	}
	
    public void loadShader(String name, ResourceLocation shadersPostLocation) {
    	/*if(this.theShaderGroup != null) {
            this.theShaderGroup.deleteShaderGroup();
        }*/
    	
        try {
            ShaderGroupCustom group = new ShaderGroupCustom(this.mc.getTextureManager(), this.resourceManager, this.mc.getFramebuffer(), shadersPostLocation);
            group.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
            groups.add(group);
            shaders.add(name);
            //this.shaderName = name;
        } catch (IOException ioexception) {
            Base.log.warn("Failed to load shader: " + shadersPostLocation, ioexception);
            //this.shaderName = "";
        } catch (JsonSyntaxException jsonsyntaxexception) {
            Base.log.warn("Failed to load shader: " + shadersPostLocation, jsonsyntaxexception);
            //this.shaderName = "";
        }
        this.useShader = groups.size() > 0;
    }

	public void drawShaders(float partialTicks) {
		
		/*mc.getRenderManager().setRenderOutlines(false);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.depthMask(false);*/
        
		if(groups.size() > 0 && this.useShader) {
			GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            
            GlStateManager.loadIdentity();
            for (int i = 0; i < groups.size(); i++) {
				groups.get(i).loadShaderGroup(partialTicks);
			}
			//Base.instance.shaderHelper.getShaderGroup().loadShaderGroup(partialTicks);
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
	
	public ShaderGroupCustom getShaderGroup(String name) {
		int index = shaders.indexOf(name);
		if(index != -1)
			return groups.get(index);
		return null;
	}

	public ArrayList<ShaderGroupCustom> getShaderGroups() {
		return groups;
	}

	public void removeShader(String name) {
		int index = shaders.indexOf(name);
		if(index != -1)
		{
			shaders.remove(index);
			groups.remove(index);
		}
		
		this.useShader = groups.size() > 0;
	}
}

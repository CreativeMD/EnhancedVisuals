package com.sonicjumper.enhancedvisuals.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.lib.GlStateManager;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;
import com.sonicjumper.enhancedvisuals.visuals.Shader;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualManager;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

@SideOnly(Side.CLIENT)
public class VisualRenderer {
	private HashMap<Class, RenderVisual> rendererMapStore;
	
	public VisualRenderer() {
		rendererMapStore = new HashMap<Class, RenderVisual>();
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderGameOverlayEvent event) {
		if(event.type == ElementType.HOTBAR && event.isCancelable()) {
			//renderStuff(event.partialTicks);
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if(event.phase == Phase.END) {
			//GlStateManager.pushMatrix();
			renderStuff(event.renderTickTime);
			//GlStateManager.enableRescaleNormal();
			
			Minecraft mc = Minecraft.getMinecraft();
			GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
	        GL11.glLoadIdentity();
	        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        GL11.glLoadIdentity();
	        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
	        GL11.glMatrixMode(GL11.GL_PROJECTION);
	        GL11.glLoadIdentity();
	        int width = mc.displayWidth;
	        int height = mc.displayHeight;
	        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
	        width = scaledresolution.getScaledWidth();
	        height = scaledresolution.getScaledHeight();
	        GL11.glOrtho(0.0D, (double)width, (double)height, 0.0D, 1000.0D, 3000.0D);
	        GL11.glMatrixMode(GL11.GL_MODELVIEW);
	        GL11.glLoadIdentity();
	        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
	        GlStateManager.enableRescaleNormal();
			//GlStateManager.loadIdentity();
			//GlStateManager.popAttrib();
			//GlStateManager.popMatrix();
		}
	}
	
	private void renderStuff(float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		if(!mc.isGamePaused())
		{
			if(Base.instance != null && Base.instance.shaderHelper != null && Base.instance.shaderHelper.getShaderGroup() != null && Base.instance.shaderHelper.getShaderGroup().needNewFrameBuffer(mc.displayWidth, mc.displayHeight))
				Base.instance.shaderHelper.getShaderGroup().createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			
			ArrayList<Visual> visualList = Base.instance.manager.getActiveVisuals();
			if(visualList.size() > 0) {
				// Base.log.info("Attempting to render " + visualList.size() + " splats");
				// TODO Find out which blend operations are necessary, and which cause the screen stretching glitch
		        //GlStateManager.pushMatrix();
				//GlStateManager.disableDepth();
		        //GlStateManager.disableLighting();
		       // GlStateManager.depthMask(false);
		        //GlStateManager.enableBlend();	
				
				
				
				GL11.glPushMatrix();
				//setupOverlayRendering();
				//GlStateManager.disableCull();
				
				//GlStateManager.disableLighting();
				//GlStateManager.enableLighting();
				//GlStateManager.resetColor();				
				//GlStateManager.enableRescaleNormal();
				//GlStateManager.enableBlend();
				//GlStateManager.enableAlpha();
				
				GL11.glDisable(GL11.GL_LIGHTING);
		        GL11.glDepthMask(false);
		        GL11.glDisable(GL11.GL_ALPHA_TEST);
		        GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				//GlStateManager.depthMask(true);
				
				//GlStateManager.disableDepth();
				
				//GlStateManager.enableBlend();
		        //GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		       // GlStateManager.enableAlpha();
				
				
				
		        //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		        //GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		        //GlStateManager.disableDepth();
		        
		        // Copied from pumpkin blur rendering
		        //setupOverlayRendering();
		        
		        //GlStateManager.depthMask(false);
		        
		        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		        GlStateManager.disableAlpha();
				
		        try{
				for(int i = 0; i < visualList.size(); i++) {
					Visual v = visualList.get(i);
					/*if(v instanceof Shader) {
						v.getRenderer().renderVisual(v, partialTicks);
					}else{*/
						v.getRenderer().renderVisual(v, partialTicks);
					//}
					
				}
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        
		        GL11.glPopMatrix();
				
				/*for(Shader s : Base.instance.manager.getActiveShaders()) {
					s.getRenderer().renderVisual(s, partialTicks);
				}*/
		        
				Base.instance.shaderHelper.drawShaders(partialTicks);
				/*
				// Copied from pumpkin blur rendering
				//GlStateManager.enableAlpha();
		        //GlStateManager.depthMask(true);
		        //GlStateManager.enableDepth();
		        
		        GlStateManager.disableLighting();
				
				//GL11.glDisable(GL11.GL_BLEND);
				//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				//GL11.glPopMatrix();
				GL11.glDisable(GL11.GL_ALPHA_TEST);
		        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		        GlStateManager.enableAlpha();
				GL11.glEnable(GL11.GL_BLEND);
		        OpenGlHelper.glBlendFunc(770, 771, 1, 0);*/
		        
		       // GL11.glPushMatrix();
			}
		}
	}
	
	public void setupOverlayRendering() {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

	public RenderVisual getRendererForClass(Class<? extends RenderVisual> clazz) {
		if(!rendererMapStore.containsKey(clazz)) {
			try {
				rendererMapStore.put(clazz, clazz.newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return rendererMapStore.get(clazz);
	}
}

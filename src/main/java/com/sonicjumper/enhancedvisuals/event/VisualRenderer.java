package com.sonicjumper.enhancedvisuals.event;

import java.util.ArrayList;
import java.util.HashMap;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VisualRenderer {
	private HashMap<Class, RenderVisual> rendererMapStore;
	
	public VisualRenderer() {
		rendererMapStore = new HashMap<Class, RenderVisual>();
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderGameOverlayEvent event) {
		if(event.getType() == ElementType.HOTBAR && event.isCancelable()) {
			//renderStuff(event.partialTicks);
		}
	}
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if(event.phase == Phase.END) {
			
			renderStuff(event.renderTickTime);
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
		        /*GlStateManager.pushMatrix();
				GlStateManager.disableDepth();
		        GlStateManager.disableLighting();
		        GlStateManager.depthMask(false);
		        GlStateManager.enableBlend();
		        GlStateManager.enableAlpha();
		        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);*/
		        // Copied from pumpkin blur rendering
		        setupOverlayRendering();
		        GlStateManager.enableBlend();
		        GlStateManager.disableDepth();
		        GlStateManager.depthMask(false);
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
				
				/*for(Shader s : Base.instance.manager.getActiveShaders()) {
					s.getRenderer().renderVisual(s, partialTicks);
				}*/
				
				Base.instance.shaderHelper.drawShaders(partialTicks);
	
				// Copied from pumpkin blur rendering
		        GlStateManager.depthMask(true);
		        GlStateManager.enableDepth();
		        GlStateManager.enableAlpha();
		        GlStateManager.disableLighting();
				
				//GL11.glDisable(GL11.GL_BLEND);
				//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				//GL11.glPopMatrix();
			}
		}
	}
	
	public void setupOverlayRendering() {
        ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        GlStateManager.clear(256);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
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

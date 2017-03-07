package com.sonicjumper.enhancedvisuals.event;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.Base;
import com.sonicjumper.enhancedvisuals.death.DeathMessages;
import com.sonicjumper.enhancedvisuals.render.RenderVisual;
import com.sonicjumper.enhancedvisuals.shaders.ShaderGroupCustom;
import com.sonicjumper.enhancedvisuals.visuals.Visual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.minecart.MinecartCollisionEvent;
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
	
	public static Minecraft mc = Minecraft.getMinecraft();
	
	private String lastRenderedMessage = null;
	
	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		if(event.phase == Phase.END) {
			if(!(mc.currentScreen instanceof GuiGameOver)){
				lastRenderedMessage = null;
				renderStuff(event.renderTickTime);
				if(mc.thePlayer == null)
					Base.instance.eventHandler.onTickInGame(Minecraft.getMinecraft().thePlayer != null);
			}else{
				if(lastRenderedMessage == null)
					lastRenderedMessage = DeathMessages.pickRandomDeathMessage();
				
				mc.fontRendererObj.drawString("\"" + lastRenderedMessage + "\"", mc.currentScreen.width/2-mc.fontRendererObj.getStringWidth(lastRenderedMessage)/2, 114, 16777215);
			}
		}
	}
	
	private void renderStuff(float partialTicks) {
		if(true)
		{
			if(Base.instance != null && Base.instance.shaderHelper != null)
			{
				ArrayList<ShaderGroupCustom> groups = Base.instance.shaderHelper.getShaderGroups();
				for (int i = 0; i < groups.size(); i++) {
					if(groups.get(i).needNewFrameBuffer(mc.displayWidth, mc.displayHeight))
						groups.get(i).createBindFramebuffers(mc.displayWidth, mc.displayHeight, mc.getFramebuffer());
				} 
			}
				
			
			ArrayList<Visual> visualList = Base.instance.manager.getActiveVisuals();
			if(visualList.size() > 0) {
				// Base.log.info("Attempting to render " + visualList.size() + " splats");
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
		        GL11.glEnable(GL11.GL_BLEND);
				
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

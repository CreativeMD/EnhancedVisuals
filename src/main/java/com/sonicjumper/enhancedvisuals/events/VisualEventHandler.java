package com.sonicjumper.enhancedvisuals.events;

import java.util.Iterator;
import java.util.List;

import com.creativemd.creativecore.common.utils.HashMapList;
import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.death.DeathMessages;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VisualEventHandler {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	private static Framebuffer buffer;
	public static String lastRenderedMessage;
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onRenderTick(RenderTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			if(!(mc.currentScreen instanceof GuiGameOver)){
				lastRenderedMessage = null;
				
				if(mc.player == null)
					onTickInGame(mc.player != null);
				
				if(mc.getFramebuffer() != buffer)
				{
					buffer = mc.getFramebuffer();
					for (int i = 0; i < VisualCategory.shader.types.size(); i++) {
						VisualCategory.shader.types.get(i).onResize(buffer);
					}
				}
				
				TextureManager manager = mc.getTextureManager();
		        ScaledResolution resolution = new ScaledResolution(mc);
		        float partialTicks = mc.getRenderPartialTicks();
				
				GlStateManager.clear(256);
		        GlStateManager.matrixMode(5889);
		        GlStateManager.loadIdentity();
		        GlStateManager.ortho(0.0D, resolution.getScaledWidth_double(), resolution.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
		        GlStateManager.matrixMode(5888);
		        GlStateManager.loadIdentity();
		        GlStateManager.translate(0.0F, 0.0F, -2000.0F);
		        
		        GlStateManager.enableBlend();
		        GlStateManager.disableDepth();
		        GlStateManager.depthMask(false);
		        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		        GlStateManager.disableAlpha();		
		        
		        renderVisuals(VisualManager.visuals.getValues(VisualCategory.splat), manager, resolution, partialTicks);
		        renderVisuals(VisualManager.visuals.getValues(VisualCategory.overlay), manager, resolution, partialTicks);
		        
		        GlStateManager.matrixMode(5890);
	            GlStateManager.pushMatrix();
	            GlStateManager.loadIdentity();
	            renderVisuals(VisualManager.visuals.getValues(VisualCategory.shader), manager, resolution, partialTicks);
	            GlStateManager.popMatrix();
		        
		        GlStateManager.depthMask(true);
		        GlStateManager.enableDepth();
		        GlStateManager.enableAlpha();
		        GlStateManager.disableLighting();
			}
		}else if(DeathMessages.enabled){
			if(lastRenderedMessage == null)
				lastRenderedMessage = DeathMessages.pickRandomDeathMessage();
			
			mc.fontRendererObj.drawString("\"" + lastRenderedMessage + "\"", mc.currentScreen.width/2-mc.fontRendererObj.getStringWidth(lastRenderedMessage)/2, 114, 16777215);
		}
	}
	
	private static void renderVisuals(List<Visual> visuals, TextureManager manager, ScaledResolution resolution, float partialTicks)
	{
		for (Iterator iterator = visuals.iterator(); iterator.hasNext();) {
			Visual visual = (Visual) iterator.next();
			float intensity = visual.getIntensity() * visual.type.alpha;
			if(intensity > 0)
				visual.type.render(visual, manager, resolution, partialTicks, intensity);
		}
	}
	
	@SubscribeEvent
	public static void onTick(ClientTickEvent event)
	{
		if(event.phase == Phase.END && !(mc.currentScreen instanceof GuiGameOver)) {
			onTickInGame(mc.player != null);
		}
	}
	
	public static void onTickInGame(boolean isInGame)
	{
		VisualManager.onTick(mc.player);
		
		for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
			VisualHandler.activeHandlers.get(i).onTick(mc.player);
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		if(event.getEntityLiving().equals(mc.player)) {
			VisualManager.clearAllVisuals();
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerDamage(LivingAttackEvent event)
	{
		if(event.getEntity() instanceof EntityPlayer)
		{
			for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
				VisualHandler.activeHandlers.get(i).onPlayerDamaged((EntityPlayer) event.getEntity(), event.getSource(), event.getAmount());
			}
		}else{
			double distance = Math.sqrt(mc.player.getDistanceSqToEntity(event.getEntity()));
			for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
				VisualHandler.activeHandlers.get(i).onEntityDamaged(event.getEntityLiving(), event.getSource(), event.getAmount(), distance);
			}
		}
	}
	
	@SubscribeEvent
	public void onSoundPlayed(SoundSourceEvent event)
	{
		if(SoundMuteHandler.isMuting && SoundMuteHandler.ignoredSounds != null)
		{
			if(event.getSound().getSoundLocation().toString().equals("enhancedvisuals:ringing"))
				SoundMuteHandler.ignoredSounds.add(event.getUuid());
		}
	}
}

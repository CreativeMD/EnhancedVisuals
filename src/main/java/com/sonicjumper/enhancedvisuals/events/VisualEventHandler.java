package com.sonicjumper.enhancedvisuals.events;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.sonicjumper.enhancedvisuals.EnhancedVisuals;
import com.sonicjumper.enhancedvisuals.VisualManager;
import com.sonicjumper.enhancedvisuals.death.DeathMessages;
import com.sonicjumper.enhancedvisuals.handlers.DamageHandler;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class VisualEventHandler {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	public static String lastRenderedMessage;
	
	private static int framebufferWidth;
	private static int framebufferHeight;
	//private static int framebufferTextureWidth;
	//private static int framebufferTextureHeight;
	
	public static boolean areEffectsEnabled() {
		return EnhancedVisuals.noEffectsForCreative ? mc.player != null && !mc.player.isCreative() && !mc.player.isSpectator() : true;
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void onRenderTick(RenderTickEvent event) {
		if (event.phase == Phase.END && areEffectsEnabled()) {
			if (!(mc.currentScreen instanceof GuiGameOver)) {
				lastRenderedMessage = null;
				
				if (mc.player == null)
					onTickInGame(mc.player != null);
				else {
					if (mc.player.maxHurtTime > 0 && mc.player.hurtTime == mc.player.maxHurtTime && DamageHandler.hitEffect)
						VisualManager.addVisualWithShading(VisualType.damaged, DamageHandler.hitEffectIntensity, DamageHandler.hitEffectMinDuration, DamageHandler.hitEffectMaxDuration, new Color(1.0F, 1.0F, 1.0F, 0.2F));
				}
				
				if (mc.getFramebuffer().framebufferWidth != framebufferWidth || mc.getFramebuffer().framebufferHeight != framebufferHeight) {
					for (int i = 0; i < VisualCategory.shader.types.size(); i++) {
						VisualCategory.shader.types.get(i).onResize(mc.getFramebuffer());
					}
					framebufferWidth = mc.getFramebuffer().framebufferWidth;
					framebufferHeight = mc.getFramebuffer().framebufferHeight;
				}
				
				GlStateManager.pushMatrix();
				
				TextureManager manager = mc.getTextureManager();
				ScaledResolution resolution = new ScaledResolution(mc);
				float partialTicks = event.renderTickTime;
				
				RenderHelper.enableStandardItemLighting();
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
				GL11.glEnable(GL11.GL_BLEND);
				
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
				
				mc.getFramebuffer().bindFramebuffer(false);
				GlStateManager.matrixMode(5888);
				
				GlStateManager.popMatrix();
			} else {
				if (areEffectsEnabled())
					VisualManager.resetAllVisuals();
				
				if (DeathMessages.enabled) {
					if (lastRenderedMessage == null)
						lastRenderedMessage = DeathMessages.pickRandomDeathMessage();
					
					if (lastRenderedMessage != null)
						mc.fontRenderer.drawString("\"" + lastRenderedMessage + "\"", mc.currentScreen.width / 2 - mc.fontRenderer.getStringWidth(lastRenderedMessage) / 2, 114, 16777215);
				}
			}
		}
	}
	
	private static void renderVisuals(List<Visual> visuals, TextureManager manager, ScaledResolution resolution, float partialTicks) {
		if (visuals == null || visuals.isEmpty())
			return;
		try {
			for (Iterator iterator = visuals.iterator(); iterator.hasNext();) {
				Visual visual = (Visual) iterator.next();
				float intensity = visual.getIntensity(partialTicks) * visual.type.alpha;
				if (visual.type.needsToBeRendered(intensity)) {
					GlStateManager.pushMatrix();
					visual.type.render(visual, manager, resolution, partialTicks, intensity);
					GlStateManager.popMatrix();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public static void onTick(ClientTickEvent event) {
		if (event.phase == Phase.END && areEffectsEnabled() && !(mc.currentScreen instanceof GuiGameOver)) {
			onTickInGame(mc.player != null);
			
			SoundMuteHandler.tick();
		}
	}
	
	public static void onTickInGame(boolean isInGame) {
		VisualManager.onTick(mc.player);
		
		for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
			VisualHandler.activeHandlers.get(i).onTick(mc.player);
		}
		
	}
	
	@SubscribeEvent
	public static void onPlayerDamage(LivingAttackEvent event) {
		if (!event.getEntityLiving().world.isRemote || !areEffectsEnabled())
			return;
		if (event.getEntity() instanceof EntityPlayer && event.getEntity() == mc.player) {
			for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
				VisualHandler.activeHandlers.get(i).onPlayerDamaged((EntityPlayer) event.getEntity(), event.getSource(), event.getAmount());
			}
		} else if (mc.player != null) {
			double distance = Math.sqrt(mc.player.getDistanceSq(event.getEntity()));
			for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
				VisualHandler.activeHandlers.get(i).onEntityDamaged(event.getEntityLiving(), event.getSource(), event.getAmount(), distance);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onSoundPlay(PlaySoundEvent event) {
		if (event.getSound().getSoundLocation().equals(SoundEvents.ENTITY_GENERIC_EXPLODE.getSoundName()) && areEffectsEnabled()) {
			//Explosion
			double distance = Math.sqrt(mc.player.getDistanceSq(event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF()));
			for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
				VisualHandler.activeHandlers.get(i).onExplosion(mc.player, event.getSound().getXPosF(), event.getSound().getYPosF(), event.getSound().getZPosF(), distance);
			}
		}
	}
	
	@SubscribeEvent
	public static void onSoundPlayed(SoundSourceEvent event) {
		if (SoundMuteHandler.isMuting && SoundMuteHandler.ignoredSounds != null) {
			if (event.getSound().getSoundLocation().toString().equals("enhancedvisuals:ringing"))
				SoundMuteHandler.ignoredSounds.add(event.getUuid());
		}
	}
	
	@SubscribeEvent
	public static void onThrowableImpact(ProjectileImpactEvent.Throwable event) {
		if (!areEffectsEnabled())
			return;
		for (int i = 0; i < VisualHandler.activeHandlers.size(); i++) {
			VisualHandler.activeHandlers.get(i).onThrowableImpact(event);
		}
	}
}

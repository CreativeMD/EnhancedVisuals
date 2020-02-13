package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;

public class EVRenderer {
	
	private static Minecraft mc = Minecraft.getInstance();
	
	private static String lastRenderedMessage;
	
	private static int framebufferWidth;
	private static int framebufferHeight;
	
	public static boolean reloadResources = false;
	
	@SubscribeEvent
	public static void render(RenderTickEvent event) {
		if (event.phase == Phase.END && EVClient.shouldRender()) {
			
			if (reloadResources) {
				for (VisualType type : VisualType.getTypes()) {
					type.loadResources(mc.getResourceManager());
				}
				reloadResources = false;
			}
			
			if (!(mc.currentScreen instanceof DeathScreen)) {
				if (mc.getFramebuffer().framebufferWidth != framebufferWidth || mc.getFramebuffer().framebufferHeight != framebufferHeight) {
					for (VisualType type : VisualType.getTypes())
						type.resize(mc.getFramebuffer());
					framebufferWidth = mc.getFramebuffer().framebufferWidth;
					framebufferHeight = mc.getFramebuffer().framebufferHeight;
				}
				
				int screenWidth = mc.mainWindow.getScaledWidth();
				int screenHeight = mc.mainWindow.getScaledHeight();
				
				GlStateManager.pushMatrix();
				TextureManager manager = mc.getTextureManager();
				float partialTicks = event.renderTickTime;
				
				RenderHelper.enableStandardItemLighting();
				GlStateManager.disableLighting();
				GlStateManager.clear(256, false);
				GlStateManager.matrixMode(5889);
				GlStateManager.loadIdentity();
				GlStateManager.ortho(0.0D, mc.mainWindow.getScaledWidth(), mc.mainWindow.getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
				GlStateManager.matrixMode(5888);
				GlStateManager.loadIdentity();
				GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
				
				GlStateManager.enableBlend();
				GlStateManager.disableDepthTest();
				GlStateManager.depthMask(false);
				GlStateManager.blendFuncSeparate(770, 771, 1, 0);
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.disableAlphaTest();
				GlStateManager.enableBlend();
				
				renderVisuals(VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
				renderVisuals(VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
				
				GlStateManager.matrixMode(5890);
				GlStateManager.pushMatrix();
				GlStateManager.loadIdentity();
				renderVisuals(VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
				GlStateManager.popMatrix();
				
				GlStateManager.depthMask(true);
				GlStateManager.enableDepthTest();
				GlStateManager.enableAlphaTest();
				GlStateManager.disableLighting();
				
				mc.getFramebuffer().bindFramebuffer(false);
				GlStateManager.matrixMode(5888);
				
				GlStateManager.popMatrix();
				
			} else {
				if (EnhancedVisuals.MESSAGES.enabled) {
					if (lastRenderedMessage == null)
						lastRenderedMessage = EnhancedVisuals.MESSAGES.pickRandomDeathMessage();
					
					if (lastRenderedMessage != null)
						mc.fontRenderer.drawString("\"" + lastRenderedMessage + "\"", mc.currentScreen.width / 2 - mc.fontRenderer.getStringWidth(lastRenderedMessage) / 2, 114, 16777215);
				}
			}
		}
	}
	
	private static void renderVisuals(Collection<Visual> visuals, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
		if (visuals == null || visuals.isEmpty())
			return;
		try {
			
			for (Visual visual : visuals) {
				if (visual.isVisible()) {
					GlStateManager.pushMatrix();
					visual.render(manager, screenWidth, screenHeight, partialTicks);
					GlStateManager.popMatrix();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

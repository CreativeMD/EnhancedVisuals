package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;

public class EVRenderer {
    
    private static Minecraft mc = Minecraft.getMinecraft();
    
    private static String lastRenderedMessage;
    
    private static int framebufferWidth;
    private static int framebufferHeight;
    
    @SubscribeEvent
    public static void render(RenderTickEvent event) {
        if (event.phase == Phase.END && EVClient.shouldRender()) {
            
            if (!(mc.currentScreen instanceof GuiGameOver)) {
                if (mc.getFramebuffer().framebufferWidth != framebufferWidth || mc.getFramebuffer().framebufferHeight != framebufferHeight) {
                    for (VisualType type : VisualType.getTypes())
                        type.resize(mc.getFramebuffer());
                    framebufferWidth = mc.getFramebuffer().framebufferWidth;
                    framebufferHeight = mc.getFramebuffer().framebufferHeight;
                }
                
                ScaledResolution resolution = new ScaledResolution(mc);
                int screenWidth = resolution.getScaledWidth();
                int screenHeight = resolution.getScaledHeight();
                
                GlStateManager.pushMatrix();
                TextureManager manager = mc.getTextureManager();
                float partialTicks = event.renderTickTime;
                
                RenderHelper.enableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.clear(256);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, screenWidth, screenHeight, 0.0D, 1000.0D, 3000.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -2000.0F);
                
                GlStateManager.disableBlend();
                GlStateManager.disableDepth();
                GlStateManager.depthMask(false);
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableAlpha();
                GlStateManager.enableBlend();
                
                renderVisuals(VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
                renderVisuals(VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
                
                GlStateManager.matrixMode(5890);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                renderVisuals(VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
                GlStateManager.popMatrix();
                
                GlStateManager.depthMask(true);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.disableLighting();
                
                mc.getFramebuffer().bindFramebuffer(false);
                GlStateManager.matrixMode(5888);
                
                GlStateManager.popMatrix();
                lastRenderedMessage = null;
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

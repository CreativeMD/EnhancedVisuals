package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.DeathScreen;
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
            
            if (!(mc.screen instanceof DeathScreen)) {
                if (mc.getMainRenderTarget().width != framebufferWidth || mc.getMainRenderTarget().height != framebufferHeight) {
                    for (VisualType type : VisualType.getTypes())
                        type.resize(mc.getMainRenderTarget());
                    framebufferWidth = mc.getMainRenderTarget().width;
                    framebufferHeight = mc.getMainRenderTarget().height;
                }
                
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                
                RenderSystem.pushMatrix();
                TextureManager manager = mc.getTextureManager();
                float partialTicks = event.renderTickTime;
                
                //RenderHelper.enableStandardItemLighting();
                RenderSystem.disableLighting();
                RenderSystem.clear(256, false);
                RenderSystem.matrixMode(5889);
                RenderSystem.loadIdentity();
                RenderSystem.ortho(0.0D, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), 0.0D, 1000.0D, 3000.0D);
                RenderSystem.matrixMode(5888);
                RenderSystem.loadIdentity();
                RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
                
                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.disableAlphaTest();
                RenderSystem.enableBlend();
                
                renderVisuals(VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
                renderVisuals(VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
                
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.disableAlphaTest();
                RenderSystem.enableTexture();
                RenderSystem.matrixMode(5890);
                RenderSystem.pushMatrix();
                RenderSystem.loadIdentity();
                renderVisuals(VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
                RenderSystem.popMatrix();
                
                RenderSystem.depthMask(true);
                RenderSystem.enableDepthTest();
                RenderSystem.enableAlphaTest();
                RenderSystem.enableBlend();
                RenderSystem.disableLighting();
                
                mc.getMainRenderTarget().bindWrite(true);
                RenderSystem.matrixMode(5888);
                
                RenderSystem.popMatrix();
                lastRenderedMessage = null;
            } else {
                if (EnhancedVisuals.MESSAGES.enabled) {
                    if (lastRenderedMessage == null)
                        lastRenderedMessage = EnhancedVisuals.MESSAGES.pickRandomDeathMessage();
                    
                    if (lastRenderedMessage != null)
                        mc.font.drawShadow(new MatrixStack(), "\"" + lastRenderedMessage + "\"", mc.screen.width / 2 - mc.font.width(lastRenderedMessage) / 2, 114, 16777215);
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
                    RenderSystem.pushMatrix();
                    visual.render(manager, screenWidth, screenHeight, partialTicks);
                    RenderSystem.popMatrix();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
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
                
                TextureManager manager = mc.getTextureManager();
                float partialTicks = event.renderTickTime;
                
                //RenderHelper.enableStandardItemLighting();
                RenderSystem.clear(256, Minecraft.ON_OSX);
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                Matrix4f matrix4f = Matrix4f.orthographic(0.0F, (float) (mc.getWindow().getWidth() / mc.getWindow().getGuiScale()), 0.0F, (float) (mc.getWindow().getHeight() / mc
                        .getWindow().getGuiScale()), 1000.0F, 3000.0F);
                RenderSystem.setProjectionMatrix(matrix4f);
                PoseStack stack = RenderSystem.getModelViewStack();
                stack.setIdentity();
                stack.translate(0.0D, 0.0D, -2000.0D);
                RenderSystem.applyModelViewMatrix();
                Lighting.setupFor3DItems();
                RenderSystem.disableTexture();
                
                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.blendFuncSeparate(770, 771, 1, 0);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                //RenderSystem.disableAlphaTest();
                RenderSystem.enableBlend();
                
                renderVisuals(stack, VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
                renderVisuals(stack, VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
                
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                //RenderSystem.disableAlphaTest();
                
                RenderSystem.disableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.enableTexture();
                RenderSystem.resetTextureMatrix();
                renderVisuals(stack, VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
                
                RenderSystem.clear(256, Minecraft.ON_OSX);
                
                mc.getMainRenderTarget().bindWrite(true);
                RenderSystem.applyModelViewMatrix();
                lastRenderedMessage = null;
            } else {
                if (EnhancedVisuals.MESSAGES.enabled) {
                    if (lastRenderedMessage == null)
                        lastRenderedMessage = EnhancedVisuals.MESSAGES.pickRandomDeathMessage();
                    
                    if (lastRenderedMessage != null)
                        mc.font.drawShadow(new PoseStack(), "\"" + lastRenderedMessage + "\"", mc.screen.width / 2 - mc.font.width(lastRenderedMessage) / 2, 114, 16777215);
                }
            }
        }
    }
    
    private static void renderVisuals(PoseStack stack, Collection<Visual> visuals, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        if (visuals == null || visuals.isEmpty())
            return;
        try {
            
            for (Visual visual : visuals) {
                if (visual.isVisible()) {
                    stack.pushPose();
                    RenderSystem.applyModelViewMatrix();
                    visual.render(stack, manager, screenWidth, screenHeight, partialTicks);
                    stack.popPose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

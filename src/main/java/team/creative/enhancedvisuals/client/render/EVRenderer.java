package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import org.joml.Matrix4f;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraftforge.client.ForgeHooksClient;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class EVRenderer {
    
    private static Minecraft mc = Minecraft.getInstance();
    
    private static String lastRenderedMessage;
    
    private static int framebufferWidth;
    private static int framebufferHeight;
    
    public static boolean reloadResources = false;
    
    public static void render() {
        if (EVClient.shouldRender()) {
            if (reloadResources) {
                for (VisualType type : VisualType.getTypes()) {
                    type.loadResources(mc.getResourceManager());
                }
                reloadResources = false;
            }
            
            if (!(mc.screen instanceof DeathScreen)) {
                
                float partialTicks = Minecraft.getInstance().getFrameTime();
                
                if (mc.player != null && mc.player.hurtDuration > 0 && mc.player.hurtTime == mc.player.hurtDuration)
                    VisualHandlers.DAMAGE.clientHurt();
                
                if (mc.getMainRenderTarget().width != framebufferWidth || mc.getMainRenderTarget().height != framebufferHeight) {
                    for (VisualType type : VisualType.getTypes())
                        type.resize(mc.getMainRenderTarget());
                    framebufferWidth = mc.getMainRenderTarget().width;
                    framebufferHeight = mc.getMainRenderTarget().height;
                }
                
                int screenWidth = mc.getWindow().getWidth();
                int screenHeight = mc.getWindow().getHeight();
                
                TextureManager manager = mc.getTextureManager();
                
                RenderSystem.clear(256, Minecraft.ON_OSX);
                Matrix4f matrix4f = (new Matrix4f()).setOrtho(0.0F, screenWidth, screenHeight, 0.0F, 1000.0F, ForgeHooksClient.getGuiFarPlane());
                RenderSystem.setProjectionMatrix(matrix4f);
                PoseStack stack = RenderSystem.getModelViewStack();
                stack.setIdentity();
                stack.translate(0.0D, 0.0D, 1000F - ForgeHooksClient.getGuiFarPlane());
                RenderSystem.applyModelViewMatrix();
                Lighting.setupFor3DItems();
                
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.enableTexture();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                
                renderVisuals(stack, VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
                renderVisuals(stack, VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
                
                RenderSystem.disableBlend();
                RenderSystem.disableTexture();
                RenderSystem.resetTextureMatrix();
                renderVisuals(stack, VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
                
                RenderSystem.clear(256, Minecraft.ON_OSX);
                
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
            
            for (Visual visual : visuals)
                if (visual.isVisible())
                    visual.render(stack, manager, screenWidth, screenHeight, partialTicks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

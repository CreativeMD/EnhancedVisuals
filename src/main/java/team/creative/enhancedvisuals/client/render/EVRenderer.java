package team.creative.enhancedvisuals.client.render;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import team.creative.creativecore.common.util.mc.ColorUtils;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;

public class EVRenderer {
    
    private static ShaderInstance positionTexColorSmoothShader;
    
    private static Minecraft mc = Minecraft.getInstance();
    
    private static String lastRenderedMessage;
    
    private static int framebufferWidth;
    private static int framebufferHeight;
    
    public static boolean reloadResources = false;
    
    public static void loadShaders(ResourceProvider provier, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaders) throws IOException {
        shaders.add(Pair.of(new ShaderInstance(provier, EnhancedVisuals.MODID + ":position_tex_col_smooth", DefaultVertexFormat.POSITION_TEX_COLOR),
            x -> positionTexColorSmoothShader = x));
    }
    
    @Nullable
    public static ShaderInstance getPositionTexColorSmoothShader() {
        return positionTexColorSmoothShader;
    }
    
    public static void render(Object object) {
        GuiGraphics graphics = (GuiGraphics) object;
        if (EVClient.shouldRender()) {
            if (reloadResources) {
                for (VisualType type : VisualType.getTypes())
                    type.loadResources(mc.getResourceManager());
                reloadResources = false;
            }
            
            if (!(mc.screen instanceof DeathScreen)) {
                //graphics.flush();
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
                Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, screenWidth, screenHeight, 0.0F, 1000.0F, 21000F);
                RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
                Matrix4fStack stack = RenderSystem.getModelViewStack();
                stack.pushMatrix();
                stack.identity();
                stack.translation(0.0F, 0.0F, -11000F);
                RenderSystem.applyModelViewMatrix();
                
                PoseStack poseStack = new PoseStack();
                Matrix4f pose = poseStack.last().pose();
                Lighting.setupFor3DItems();
                
                poseStack.translate(100, 100, 0);
                
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                
                BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
                bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                int color = ColorUtils.BLACK;
                int z = -90;
                
                bufferbuilder.vertex(pose, screenWidth, screenHeight, z).color(color).endVertex();
                bufferbuilder.vertex(pose, screenWidth, 0, z).color(color).endVertex();
                bufferbuilder.vertex(pose, 0, 0, z).color(color).endVertex();
                bufferbuilder.vertex(pose, 0, screenHeight, z).color(color).endVertex();
                BufferUploader.drawWithShader(bufferbuilder.end());
                
                RenderSystem.disableBlend();
                RenderSystem.resetTextureMatrix();
                RenderSystem.disableDepthTest();
                renderVisuals(poseStack, VisualManager.visuals(VisualCategory.shader), manager, screenWidth, screenHeight, partialTicks);
                
                mc.getMainRenderTarget().bindWrite(true);
                
                RenderSystem.clear(256, Minecraft.ON_OSX);
                RenderSystem.enableBlend();
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                
                renderVisuals(poseStack, VisualManager.visuals(VisualCategory.overlay), manager, screenWidth, screenHeight, partialTicks);
                renderVisuals(poseStack, VisualManager.visuals(VisualCategory.particle), manager, screenWidth, screenHeight, partialTicks);
                
                lastRenderedMessage = null;
                
                graphics.flush();
                
                Window window = mc.getWindow();
                RenderSystem.clear(256, Minecraft.ON_OSX);
                RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0.0F, (float) (window.getWidth() / window.getGuiScale()), (float) (window.getHeight() / window
                        .getGuiScale()), 0.0F, 1000.0F, 21000F), VertexSorting.ORTHOGRAPHIC_Z);
                stack.popMatrix();
                RenderSystem.applyModelViewMatrix();
                Lighting.setupFor3DItems();
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                
                RenderSystem.depthMask(true);
            } else {
                if (EnhancedVisuals.MESSAGES.enabled) {
                    if (lastRenderedMessage == null)
                        lastRenderedMessage = EnhancedVisuals.MESSAGES.pickRandomDeathMessage();
                    
                    if (lastRenderedMessage != null)
                        graphics.drawString(mc.font, "\"" + lastRenderedMessage + "\"", mc.screen.width / 2 - mc.font.width(lastRenderedMessage) / 2, 114, 16777215);
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

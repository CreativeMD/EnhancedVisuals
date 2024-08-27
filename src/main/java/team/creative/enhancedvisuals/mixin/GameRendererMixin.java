package team.creative.enhancedvisuals.mixin;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.shaders.Program;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.GlStateBackup;
import team.creative.creativecore.common.util.mc.ColorUtils;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    
    private static final GlStateBackup stateBackup = new GlStateBackup();
    
    @Inject(method = "reloadShaders(Lnet/minecraft/server/packs/resources/ResourceProvider;)V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/GameRenderer;shutdownShaders()V"), locals = LocalCapture.CAPTURE_FAILHARD, require = 1)
    private void reloadShaders(ResourceProvider provier, CallbackInfo info, List<Program> programs, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaders) {
        try {
            EVRenderer.loadShaders(provier, shaders);
        } catch (IOException e) {
            EnhancedVisuals.LOGGER.error(e);
        }
    }
    
    @Inject(method = "processBlurEffect(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V"), require = 1)
    public void processBlurEffect(float partialTicks, CallbackInfo info) {
        RenderSystem.backupGlState(stateBackup);
        
        Minecraft mc = Minecraft.getInstance();
        
        int screenWidth = mc.getWindow().getWidth();
        int screenHeight = mc.getWindow().getHeight();
        
        PoseStack poseStack = new PoseStack();
        Matrix4f pose = poseStack.last().pose();
        
        var shader = RenderSystem.getShader();
        
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        int color = ColorUtils.BLACK;
        int z = -90;
        
        bufferbuilder.addVertex(pose, screenWidth, screenHeight, z).setColor(color);
        bufferbuilder.addVertex(pose, screenWidth, 0, z).setColor(color);
        bufferbuilder.addVertex(pose, 0, 0, z).setColor(color);
        bufferbuilder.addVertex(pose, 0, screenHeight, z).setColor(color);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
        
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(() -> shader);
        
        RenderSystem.restoreGlState(stateBackup);
    }
    
}

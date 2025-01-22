package team.creative.enhancedvisuals.mixin;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.GameRenderer;
import team.creative.creativecore.client.render.CreativePlatformHooks;
import team.creative.creativecore.common.util.mc.ColorUtils;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.client.mc.GameRendererExtender;

@Mixin(GameRenderer.class)
public class GameRendererMixin implements GameRendererExtender {
    
    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;
    
    @Inject(method = "processBlurEffect()V", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/PostChain;process(Lcom/mojang/blaze3d/pipeline/RenderTarget;Lcom/mojang/blaze3d/resource/GraphicsResourceAllocator;)V"),
            require = 1)
    public void processBlurEffect(CallbackInfo info) {
        if (!EnhancedVisuals.CONFIG.fixBlurShader)
            return;
        CreativePlatformHooks.backupRenderState();
        
        Minecraft mc = Minecraft.getInstance();
        
        int screenWidth = mc.getWindow().getWidth();
        int screenHeight = mc.getWindow().getHeight();
        
        Matrix4f pose = new Matrix4f();
        var shader = RenderSystem.getShader();
        
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
            GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1F);
        RenderSystem.setShader(CoreShaders.POSITION_COLOR);
        
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
        RenderSystem.setShader(shader);
        
        CreativePlatformHooks.restoreRenderState();
    }
    
    @Override
    public CrossFrameResourcePool getResourcePool() {
        return resourcePool;
    }
    
}

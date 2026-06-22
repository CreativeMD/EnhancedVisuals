package team.creative.enhancedvisuals.client.type;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.server.packs.resources.ResourceManager;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualTypeShader;

public abstract class VisualTypeShaderClient<T extends VisualTypeShader> extends VisualTypeClient<T> {
    
    public PostChain postChain;
    
    public VisualTypeShaderClient(T type) {
        super(type);
    }
    
    @Override
    public void loadResources(ResourceManager manager) {}
    
    @Override
    public int getVariantAmount() {
        return 0;
    }
    
    @Override
    public void resize(RenderTarget buffer) {}
    
    @Override
    public void render(GuiGraphicsExtractor graphics, VisualHandler handler, Visual visual, int screenWidth, int screenHeight, float partialTicks) {
        var mc = Minecraft.getInstance();
        if (postChain == null)
            postChain = mc.getShaderManager().getPostChain(type.identifier, LevelTargetBundle.MAIN_TARGETS);
        if (postChain != null) {
            changeProperties(visual.getOpacity());
            postChain.process(mc.gameRenderer.mainRenderTarget(), GraphicsResourceAllocator.UNPOOLED); // ((GameRendererExtender) mc.gameRenderer).getResourcePool()
        }
    }
    
    public abstract void changeProperties(float intensity);
    
}

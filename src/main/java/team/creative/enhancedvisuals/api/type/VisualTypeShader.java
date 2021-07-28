package team.creative.enhancedvisuals.api.type;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.render.EnhancedPostChain;

public abstract class VisualTypeShader extends VisualType {
    
    public ResourceLocation location;
    
    public VisualTypeShader(String name, ResourceLocation location) {
        super(name, VisualCategory.shader);
        this.location = location;
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public EnhancedPostChain postChain;
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void loadResources(ResourceManager manager) {
        Minecraft mc = Minecraft.getInstance();
        if (postChain != null)
            postChain.close();
        
        try {
            if (mc.isSameThread()) {
                postChain = new EnhancedPostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), location);
                postChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
            }
        } catch (JsonSyntaxException | IOException e) {}
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public int getVariantAmount() {
        return 0;
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public void resize(RenderTarget buffer) {
        if (postChain != null)
            postChain.resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
    }
    
    @Override
    public void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        if (postChain == null)
            loadResources(Minecraft.getInstance().getResourceManager());
        if (postChain != null) {
            changeProperties(visual.getOpacity());
            postChain.process(partialTicks);
        }
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public abstract void changeProperties(float intensity);
    
}

package team.creative.enhancedvisuals.api.type;

import java.io.IOException;

import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.client.render.EnhancedShaderGroup;

public abstract class VisualTypeShader extends VisualType {
    
    public ResourceLocation location;
    
    public VisualTypeShader(String name, ResourceLocation location) {
        super(name, VisualCategory.shader);
        this.location = location;
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public EnhancedShaderGroup shaderGroup;
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void loadResources(IResourceManager manager) {
        Minecraft mc = Minecraft.getInstance();
        if (shaderGroup != null)
            shaderGroup.close();
        
        try {
            if (mc.isOnExecutionThread()) {
                shaderGroup = new EnhancedShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), location);
                shaderGroup.createBindFramebuffers(mc.getMainWindow().getFramebufferWidth(), mc.getMainWindow().getFramebufferHeight());
            }
        } catch (JsonSyntaxException | IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public int getVariantAmount() {
        return 0;
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public boolean supportsColor() {
        return false;
    }
    
    @Override
    @OnlyIn(value = Dist.CLIENT)
    public void resize(Framebuffer buffer) {
        if (shaderGroup != null)
            shaderGroup.createBindFramebuffers(Minecraft.getInstance().getMainWindow().getFramebufferWidth(), Minecraft.getInstance().getMainWindow().getFramebufferHeight());
    }
    
    @Override
    public void render(Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        if (shaderGroup == null)
            loadResources(Minecraft.getInstance().getResourceManager());
        if (shaderGroup != null) {
            changeProperties(visual.opacity);
            shaderGroup.render(partialTicks);
        }
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public abstract void changeProperties(float intensity);
    
}

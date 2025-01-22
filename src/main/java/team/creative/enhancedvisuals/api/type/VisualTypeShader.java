package team.creative.enhancedvisuals.api.type;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.mc.GameRendererExtender;

public abstract class VisualTypeShader extends VisualType {
    
    public ResourceLocation location;
    
    public VisualTypeShader(String name, ResourceLocation location) {
        super(name, VisualCategory.shader);
        this.location = location;
    }
    
    public Object postChain;
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void loadResources(ResourceManager manager) {
        Minecraft mc = Minecraft.getInstance();
        //if (postChain != null)
        //    ((PostChain) postChain).close();
        
        //try {
        if (mc.isSameThread()) {
            postChain = mc.getShaderManager().getPostChain(location, LevelTargetBundle.MAIN_TARGETS);
            //((PostChain) postChain).resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        }
        //} catch (JsonSyntaxException | IOException e) {}
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public int getVariantAmount() {
        return 0;
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void resize(RenderTarget buffer) {
        //if (postChain != null)
        //     ((PostChain) postChain).resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack pose, VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks) {
        var mc = Minecraft.getInstance();
        if (postChain == null)
            loadResources(mc.getResourceManager());
        if (postChain != null) {
            changeProperties(visual.getOpacity());
            ((PostChain) postChain).process(mc.getMainRenderTarget(), ((GameRendererExtender) mc.gameRenderer).getResourcePool());
        }
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public abstract void changeProperties(float intensity);
}

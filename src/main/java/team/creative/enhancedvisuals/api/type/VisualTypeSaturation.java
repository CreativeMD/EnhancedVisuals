package team.creative.enhancedvisuals.api.type;

import com.mojang.blaze3d.shaders.Uniform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;

public class VisualTypeSaturation extends VisualTypeShader {
    
    public VisualTypeSaturation(String name) {
        super(name, ResourceLocation.tryBuild(EnhancedVisuals.MODID, "shaders/post/desaturate.json"));
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void changeProperties(float intensity) {
        for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getEffect().getUniform("Saturation");
            
            if (shaderuniform != null)
                shaderuniform.set(intensity);
        }
    }
    
    @Override
    public boolean isVisible(VisualHandler handler, Visual visual) {
        return visual.getOpacity() != 1;
    }
}

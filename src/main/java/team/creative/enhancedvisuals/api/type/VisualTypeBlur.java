package team.creative.enhancedvisuals.api.type;

import com.mojang.blaze3d.shaders.Uniform;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;

public class VisualTypeBlur extends VisualTypeShader {
    
    public static final ResourceLocation BLUR_SHADER = new ResourceLocation("shaders/post/blur.json");
    
    public VisualTypeBlur(String name) {
        super(name, BLUR_SHADER);
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void changeProperties(float intensity) {
        for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getEffect().getUniform("Radius");
            
            if (shaderuniform != null)
                shaderuniform.set(Math.max(1, (float) Math.floor(intensity)));
        }
    }
    
}

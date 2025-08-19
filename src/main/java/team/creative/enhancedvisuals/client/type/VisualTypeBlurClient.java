package team.creative.enhancedvisuals.client.type;

import com.mojang.blaze3d.opengl.Uniform;

import net.minecraft.client.renderer.PostPass;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.mixin.PostChainAccessor;

public class VisualTypeBlurClient extends VisualTypeShaderClient<VisualTypeBlur> {
    
    public VisualTypeBlurClient(VisualTypeBlur type) {
        super(type);
    }
    
    @Override
    public void changeProperties(float intensity) {
        for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getShader().getUniform("Radius");
            
            if (shaderuniform != null)
                shaderuniform.set(Math.max(1, (float) Math.floor(intensity)));
        }
    }
    
}

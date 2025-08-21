package team.creative.enhancedvisuals.client.type;

import team.creative.enhancedvisuals.api.type.VisualTypeSaturation;

public class VisualTypeSaturationClient extends VisualTypeShaderClient<VisualTypeSaturation> {
    
    public VisualTypeSaturationClient(VisualTypeSaturation type) {
        super(type);
    }
    
    @Override
    public void changeProperties(float intensity) {
        /*for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getShader().getUniform("Saturation");
            
            if (shaderuniform != null)
                shaderuniform.set(intensity);
        }*/
    }
}

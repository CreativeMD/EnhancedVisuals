package team.creative.enhancedvisuals.client.type;

import team.creative.enhancedvisuals.api.type.VisualTypeBlur;

public class VisualTypeBlurClient extends VisualTypeShaderClient<VisualTypeBlur> {
    
    public VisualTypeBlurClient(VisualTypeBlur type) {
        super(type);
    }
    
    @Override
    public void changeProperties(float intensity) {
        /*for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getShader().getUniform("Radius");
            
            if (shaderuniform != null)
                shaderuniform.set(Math.max(1, (float) Math.floor(intensity)));
        }*/
    }
    
}

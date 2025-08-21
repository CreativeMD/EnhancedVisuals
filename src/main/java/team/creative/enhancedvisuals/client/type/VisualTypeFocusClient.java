package team.creative.enhancedvisuals.client.type;

import team.creative.enhancedvisuals.api.type.VisualTypeFocus;

public class VisualTypeFocusClient extends VisualTypeShaderClient<VisualTypeFocus> {
    
    public VisualTypeFocusClient(VisualTypeFocus type) {
        super(type);
    }
    
    @Override
    public void changeProperties(float intensity) {
        /*for (PostPass pass : ((PostChainAccessor) postChain).getPasses()) {
            Uniform shaderuniform = pass.getShader().getUniform("Radius");
            
            if (shaderuniform != null)
                shaderuniform.set(intensity);
        }*/
    }
    
}

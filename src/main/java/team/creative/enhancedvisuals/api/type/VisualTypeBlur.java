package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;

public class VisualTypeBlur extends VisualTypeShader {
    
    public static final ResourceLocation BLUR_SHADER = ResourceLocation.withDefaultNamespace("blur"); //ResourceLocation.withDefaultNamespace("post_effect/blur");
    
    public VisualTypeBlur(String name) {
        super(name, BLUR_SHADER);
    }
    
}

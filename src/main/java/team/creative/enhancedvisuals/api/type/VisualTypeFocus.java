package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;

public class VisualTypeFocus extends VisualTypeShader {
    
    public static final ResourceLocation LOCATION = ResourceLocation.withDefaultNamespace("shaders/post/blobs2.json");
    
    public VisualTypeFocus(String name) {
        super(name, LOCATION);
    }
    
}

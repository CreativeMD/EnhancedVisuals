package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;

public class VisualTypeFocus extends VisualTypeShader {
    
    public static final ResourceLocation LOCATION = ResourceLocation.tryBuild(EnhancedVisuals.MODID, "shaders/post/blobs2.json");
    
    public VisualTypeFocus(String name) {
        super(name, LOCATION);
    }
    
}

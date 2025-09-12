package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;

public class VisualTypeBlur extends VisualTypeShader {
    
    public static final ResourceLocation BLUR_SHADER = ResourceLocation.tryBuild(EnhancedVisuals.MODID, "blur");
    
    public VisualTypeBlur(String name) {
        super(name, BLUR_SHADER);
    }
    
}

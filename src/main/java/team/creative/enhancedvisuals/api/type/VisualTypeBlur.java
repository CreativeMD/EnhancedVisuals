package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.EnhancedVisuals;

public class VisualTypeBlur extends VisualTypeShader {
    
    public static final Identifier BLUR_SHADER = Identifier.tryBuild(EnhancedVisuals.MODID, "blur");
    
    public VisualTypeBlur(String name) {
        super(name, BLUR_SHADER);
    }
    
}

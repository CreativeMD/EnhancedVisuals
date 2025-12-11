package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.EnhancedVisuals;

public class VisualTypeFocus extends VisualTypeShader {
    
    public static final Identifier IDENTIFIER = Identifier.tryBuild(EnhancedVisuals.MODID, "blobs2");
    
    public VisualTypeFocus(String name) {
        super(name, IDENTIFIER);
    }
    
}

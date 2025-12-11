package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;

public class VisualTypeSaturation extends VisualTypeShader {
    
    public VisualTypeSaturation(String name) {
        super(name, Identifier.tryBuild(EnhancedVisuals.MODID, "desaturate"));
    }
    
    @Override
    public boolean isVisible(VisualHandler handler, Visual visual) {
        return visual.getOpacity() != 1 && !disabled;
    }
}

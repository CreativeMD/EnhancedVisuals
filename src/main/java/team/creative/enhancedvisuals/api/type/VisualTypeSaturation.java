package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;

public class VisualTypeSaturation extends VisualTypeShader {
    
    public VisualTypeSaturation(String name) {
        super(name, ResourceLocation.tryBuild(EnhancedVisuals.MODID, "desaturate"));
    }
    
    @Override
    public boolean isVisible(VisualHandler handler, Visual visual) {
        return visual.getOpacity() != 1 && !disabled;
    }
}

package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeShader extends VisualType {
    
    public ResourceLocation location;
    
    public VisualTypeShader(String name, ResourceLocation location) {
        super(name, VisualCategory.shader);
        this.location = location;
    }
    
}

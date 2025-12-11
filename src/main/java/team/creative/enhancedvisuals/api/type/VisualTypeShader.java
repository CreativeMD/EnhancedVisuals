package team.creative.enhancedvisuals.api.type;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeShader extends VisualType {
    
    public Identifier identifier;
    
    public VisualTypeShader(String name, Identifier identifier) {
        super(name, VisualCategory.shader);
        this.identifier = identifier;
    }
    
}

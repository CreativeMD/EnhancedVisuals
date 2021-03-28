package team.creative.enhancedvisuals.api.type;

import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeParticle extends VisualTypeTexture {
    
    public VisualTypeParticle(String name, int animationSpeed, float scale) {
        super(VisualCategory.particle, name, animationSpeed, scale);
    }
    
    public VisualTypeParticle(String name, float scale) {
        this(name, 0, scale);
    }
    
    @Override
    public boolean scaleVariants() {
        return true;
    }
}

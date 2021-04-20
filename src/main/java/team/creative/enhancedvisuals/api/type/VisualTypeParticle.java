package team.creative.enhancedvisuals.api.type;

import java.util.Random;

import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.premade.DecimalMinMax;
import team.creative.enhancedvisuals.api.VisualCategory;

public class VisualTypeParticle extends VisualTypeTexture {
    
    @CreativeConfig
    public DecimalMinMax scale;
    
    public VisualTypeParticle(String name, int animationSpeed, DecimalMinMax scale) {
        super(VisualCategory.particle, name, animationSpeed);
        this.scale = scale;
    }
    
    public VisualTypeParticle(String name) {
        this(name, 0, new DecimalMinMax(0.2, 1));
    }
    
    @Override
    public boolean scaleVariants() {
        return true;
    }
    
    @Override
    public double randomScale(Random rand) {
        return scale.next(rand);
    }
}

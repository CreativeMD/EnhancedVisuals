package team.creative.enhancedvisuals.api.type;

import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.VisualCategory;

public abstract class VisualTypeTexture extends VisualType {
    
    @CreativeConfig
    public int animationSpeed;
    public String domain;
    
    public VisualTypeTexture(VisualCategory category, String name, String domain, int animationSpeed) {
        super(name, category);
        this.domain = domain;
        this.animationSpeed = animationSpeed;
    }
    
    public VisualTypeTexture(VisualCategory category, String name, int animationSpeed) {
        this(category, name, null, animationSpeed);
    }
    
}

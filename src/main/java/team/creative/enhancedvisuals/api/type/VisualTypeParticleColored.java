package team.creative.enhancedvisuals.api.type;

import java.awt.Color;

import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.CreativeConfig.IntRange;
import team.creative.creativecore.common.config.premade.DecimalMinMax;

public class VisualTypeParticleColored extends VisualTypeParticle {
    
    @CreativeConfig
    @IntRange(min = 0, max = 255)
    public int red;
    
    @CreativeConfig
    @IntRange(min = 0, max = 255)
    public int green;
    
    @CreativeConfig
    @IntRange(min = 0, max = 255)
    public int blue;
    
    @CreativeConfig
    @IntRange(min = 0, max = 255)
    public int alpha;
    
    protected Color cached;
    
    public VisualTypeParticleColored(String name, int animationSpeed, DecimalMinMax scale, Color defaultColor) {
        super(name, animationSpeed, scale);
        this.red = defaultColor.getRed();
        this.green = defaultColor.getGreen();
        this.blue = defaultColor.getBlue();
        this.alpha = defaultColor.getAlpha();
        this.cached = defaultColor;
    }
    
    public VisualTypeParticleColored(String name, Color defaultColor) {
        this(name, 0, new DecimalMinMax(0.2, 1), defaultColor);
    }
    
    @Override
    public void configured() {
        this.cached = new Color(red, green, blue, alpha);
    }
    
    @Override
    public Color getColor() {
        return cached;
    }
    
}

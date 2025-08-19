package team.creative.enhancedvisuals.api;

import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.enhancedvisuals.api.type.VisualType;

public class Particle extends Visual {
    
    public int x;
    public int y;
    
    public int width;
    public int height;
    
    public float rotation;
    
    public Particle(VisualType type, VisualHandler handler, Curve animation, int x, int y, int width, int height, float rotation, int variant) {
        super(type, handler, animation, variant);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
    
    public Particle(VisualType type, VisualHandler handler, int x, int y, int width, int height, float rotation, int variant) {
        super(type, handler, variant);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }
    
    @Override
    public int getWidth(int screenWidth) {
        return width;
    }
    
    @Override
    public int getHeight(int screenHeight) {
        return height;
    }
    
}

package team.creative.enhancedvisuals.client.render;

import net.minecraft.resources.Identifier;

public class TextureCacheAnimation extends TextureCache {
    
    public final Identifier[] identifiers;
    public final int animationSpeed;
    
    public TextureCacheAnimation(Identifier[] identifiers, int animationSpeed) {
        this.identifiers = identifiers;
        this.animationSpeed = animationSpeed;
    }
    
    @Override
    public Identifier getFirst() {
        return identifiers[0];
    }
    
    @Override
    public Identifier getResource() {
        long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
        return identifiers[(int) (time % identifiers.length)];
    }
    
}

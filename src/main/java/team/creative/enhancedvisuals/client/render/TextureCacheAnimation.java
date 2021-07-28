package team.creative.enhancedvisuals.client.render;

import net.minecraft.resources.ResourceLocation;

public class TextureCacheAnimation extends TextureCache {
    
    public final ResourceLocation[] locations;
    public final int animationSpeed;
    
    public TextureCacheAnimation(ResourceLocation[] locations, int animationSpeed) {
        this.locations = locations;
        this.animationSpeed = animationSpeed;
    }
    
    @Override
    public ResourceLocation getFirst() {
        return locations[0];
    }
    
    @Override
    public ResourceLocation getResource() {
        long time = Math.abs(System.nanoTime() / 3000000 / animationSpeed);
        return locations[(int) (time % locations.length)];
    }
    
}

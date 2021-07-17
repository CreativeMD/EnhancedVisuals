package team.creative.enhancedvisuals.client.render;

import net.minecraft.util.ResourceLocation;

public class TextureCacheSimple extends TextureCache {
    
    public final ResourceLocation location;
    
    public TextureCacheSimple(ResourceLocation location) {
        this.location = location;
    }
    
    @Override
    public ResourceLocation getResource() {
        return location;
    }
    
    @Override
    public ResourceLocation getFirst() {
        return location;
    }
    
}

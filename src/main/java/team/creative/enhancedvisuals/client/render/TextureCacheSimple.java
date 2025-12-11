package team.creative.enhancedvisuals.client.render;

import net.minecraft.resources.Identifier;

public class TextureCacheSimple extends TextureCache {
    
    public final Identifier identifier;
    
    public TextureCacheSimple(Identifier identifier) {
        this.identifier = identifier;
    }
    
    @Override
    public Identifier getResource() {
        return identifier;
    }
    
    @Override
    public Identifier getFirst() {
        return identifier;
    }
    
}

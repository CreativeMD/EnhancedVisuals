package team.creative.enhancedvisuals.api.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;

public abstract class VisualType implements ICreativeConfig {
    
    private static List<VisualType> types = new ArrayList<>();
    
    public static Collection<VisualType> getTypes() {
        return types;
    }
    
    @CreativeConfig
    public boolean disabled = false;
    
    @CreativeConfig
    @CreativeConfig.DecimalRange(max = 1, min = 0)
    public float opacity = 1;
    
    public final String name;
    public final VisualCategory cat;
    
    public VisualType(String name, VisualCategory cat) {
        this.name = name;
        this.cat = cat;
        
        types.add(this);
    }
    
    public boolean isAffectedByWater() {
        return cat.isAffectedByWater();
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public abstract void loadResources(IResourceManager manager);
    
    @OnlyIn(value = Dist.CLIENT)
    public abstract void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks);
    
    @Override
    public void configured() {
        
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public int getVariantAmount() {
        return 1;
    }
    
    public boolean supportsColor() {
        return false;
    }
    
    public Color getColor() {
        return null;
    }
    
    @OnlyIn(value = Dist.CLIENT)
    public void resize(Framebuffer buffer) {
        
    }
    
    public boolean isVisible(VisualHandler handler, Visual visual) {
        return visual.getOpacity() > 0;
    }
    
    public int getWidth(int screenWidth) {
        return screenWidth;
    }
    
    public int getHeight(int screenHeight) {
        return screenHeight;
    }
    
}

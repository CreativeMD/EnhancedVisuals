package team.creative.enhancedvisuals.api.type;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.creativemd.creativecore.common.config.api.CreativeConfig;
import com.creativemd.creativecore.common.config.api.ICreativeConfig;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.shader.Framebuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
    
    private boolean isEffectedByWater = true;
    
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
    
    public VisualType setIgnoreWater() {
        isEffectedByWater = false;
        return this;
    }
    
    public boolean isAffectedByWater() {
        return cat.isAffectedByWater() && isEffectedByWater;
    }
    
    @SideOnly(Side.CLIENT)
    public abstract void loadResources(IResourceManager manager);
    
    @SideOnly(Side.CLIENT)
    public abstract void render(VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks);
    
    @Override
    public void configured() {
        
    }
    
    @SideOnly(Side.CLIENT)
    public int getVariantAmount() {
        return 1;
    }
    
    public Color getColor() {
        return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void resize(Framebuffer buffer) {
        
    }
    
    public boolean isVisible(VisualHandler handler, Visual visual) {
        return visual.getOpacity() > 0;
    }
    
    public boolean scaleVariants() {
        return false;
    }
    
    public double randomScale(Random rand) {
        return 1;
    }
    
    public int getWidth(int screenWidth) {
        return screenWidth;
    }
    
    public int getHeight(int screenHeight) {
        return screenHeight;
    }
    
}

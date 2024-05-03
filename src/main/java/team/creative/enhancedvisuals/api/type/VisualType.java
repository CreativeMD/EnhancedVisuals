package team.creative.enhancedvisuals.api.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;
import team.creative.creativecore.common.util.type.Color;
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
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public abstract void loadResources(ResourceManager manager);
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public abstract void render(PoseStack pose, VisualHandler handler, Visual visual, TextureManager manager, int screenWidth, int screenHeight, float partialTicks);
    
    @Override
    public void configured(Side side) {}
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public int getVariantAmount() {
        return 1;
    }
    
    public Color getColor() {
        return null;
    }
    
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void resize(RenderTarget buffer) {}
    
    public boolean canRotate() {
        return true;
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
    
    public int getWidth(int screenWidth, int screenHeight) {
        return screenWidth;
    }
    
    public int getHeight(int screenWidth, int screenHeight) {
        return screenHeight;
    }
    
}

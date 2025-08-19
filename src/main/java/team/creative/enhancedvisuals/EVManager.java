package team.creative.enhancedvisuals;

import java.util.Collection;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.util.type.Color;
import team.creative.enhancedvisuals.api.Particle;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;

public interface EVManager {
    
    public Random random();
    
    public void playSound(ResourceLocation location);
    
    public void playSound(ResourceLocation location, BlockPos pos);
    
    public void playSound(ResourceLocation location, float volume);
    
    public void playSound(ResourceLocation location, BlockPos pos, float volume);
    
    public void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume);
    
    public Collection<Visual> visuals(VisualCategory category);
    
    public void clearEverything();
    
    public void add(Visual visual);
    
    public boolean remove(Visual visual);
    
    public void playTicking(ResourceLocation location, BlockPos pos, DecimalCurve volume);
    
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, IntMinMax time);
    
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, int time);
    
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, Curve curve);
    
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate);
    
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate, @Nullable Color color);
    
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, int time, boolean rotate);
    
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, Curve curve, boolean rotate, @Nullable Color color);
    
    public Particle addParticle(VisualType vt, VisualHandler handler, boolean rotate, @Nullable Color color);
    
    public int generateOffset(Random rand, int dimensionLength, int spacingBuffer);
    
}

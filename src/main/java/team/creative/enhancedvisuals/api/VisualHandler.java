package team.creative.enhancedvisuals.api;

import java.util.Collection;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.Side;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.creativecore.common.config.api.ICreativeConfig;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.util.type.Color;
import team.creative.enhancedvisuals.EVManager;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.type.VisualType;

public class VisualHandler implements ICreativeConfig, EVManager {
    
    @CreativeConfig
    public boolean enabled = true;
    
    @CreativeConfig
    @CreativeConfig.DecimalRange(max = 1, min = 0)
    public float opacity = 1;
    
    @Override
    public void configured(Side side) {}
    
    public void tick(@Nullable Player player) {}
    
    public boolean isEnabled(@Nullable Player player) {
        return enabled && opacity > 0;
    }
    
    public EVManager manager() {
        return EnhancedVisuals.MANAGER;
    }
    
    @Override
    public Random random() {
        return manager().random();
    }
    
    @Override
    public void playSound(ResourceLocation location) {
        manager().playSound(location);
    }
    
    @Override
    public void playSound(ResourceLocation location, BlockPos pos) {
        manager().playSound(location, pos);
    }
    
    @Override
    public void playSound(ResourceLocation location, float volume) {
        manager().playSound(location, volume);
    }
    
    @Override
    public void playSound(ResourceLocation location, BlockPos pos, float volume) {
        manager().playSound(location, pos, volume);
    }
    
    @Override
    public void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
        manager().playSoundFadeOut(location, pos, volume);
    }
    
    @Override
    public Collection<Visual> visuals(VisualCategory category) {
        return manager().visuals(category);
    }
    
    @Override
    public void clearEverything() {
        manager().clearEverything();
    }
    
    @Override
    public void add(Visual visual) {
        manager().add(visual);
    }
    
    @Override
    public boolean remove(Visual visual) {
        return manager().remove(visual);
    }
    
    @Override
    public void playTicking(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
        manager().playTicking(location, pos, volume);
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, IntMinMax time) {
        return manager().addVisualFadeOut(vt, handler, time);
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, int time) {
        return manager().addVisualFadeOut(vt, handler, time);
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, Curve curve) {
        return manager().addVisualFadeOut(vt, handler, curve);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate) {
        manager().addParticlesFadeOut(vt, handler, count, time, rotate);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate, @Nullable Color color) {
        manager().addParticlesFadeOut(vt, handler, count, time, rotate, color);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, int time, boolean rotate) {
        manager().addParticlesFadeOut(vt, handler, count, time, rotate);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, Curve curve, boolean rotate, @Nullable Color color) {
        manager().addParticlesFadeOut(vt, handler, count, curve, rotate, color);
    }
    
    @Override
    public Particle addParticle(VisualType vt, VisualHandler handler, boolean rotate, @Nullable Color color) {
        return manager().addParticle(vt, handler, rotate, color);
    }
    
    @Override
    public int generateOffset(Random rand, int dimensionLength, int spacingBuffer) {
        return manager().generateOffset(rand, dimensionLength, spacingBuffer);
    }
    
}

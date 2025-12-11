package team.creative.enhancedvisuals.server;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.util.type.Color;
import team.creative.enhancedvisuals.EVManager;
import team.creative.enhancedvisuals.api.Particle;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;

public class EVManagerServer implements EVManager {
    
    private final Random random = new Random();
    
    @Override
    public void playSound(Identifier identifier) {}
    
    @Override
    public void playSound(Identifier identifier, BlockPos pos) {}
    
    @Override
    public void playSound(Identifier identifier, float volume) {}
    
    @Override
    public void playSound(Identifier identifier, BlockPos pos, float volume) {}
    
    @Override
    public void playSoundFadeOut(Identifier identifier, BlockPos pos, DecimalCurve volume) {}
    
    @Override
    public Collection<Visual> visuals(VisualCategory category) {
        return Collections.EMPTY_LIST;
    }
    
    @Override
    public void clearEverything() {}
    
    @Override
    public void add(Visual visual) {}
    
    @Override
    public boolean remove(Visual visual) {
        return false;
    }
    
    @Override
    public void playTicking(Identifier identifier, BlockPos pos, DecimalCurve volume) {}
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, IntMinMax time) {
        return null;
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, int time) {
        return null;
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, Curve curve) {
        return null;
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate) {}
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate, @Nullable Color color) {}
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, int time, boolean rotate) {}
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, Curve curve, boolean rotate, @Nullable Color color) {}
    
    @Override
    public Particle addParticle(VisualType vt, VisualHandler handler, boolean rotate, @Nullable Color color) {
        return null;
    }
    
    @Override
    public int generateOffset(Random rand, int dimensionLength, int spacingBuffer) {
        return 0;
    }
    
    @Override
    public Random random() {
        return random;
    }
    
}

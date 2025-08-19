package team.creative.enhancedvisuals.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.premade.IntMinMax;
import team.creative.creativecore.common.config.premade.curve.Curve;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;
import team.creative.creativecore.common.util.type.Color;
import team.creative.creativecore.common.util.type.map.HashMapList;
import team.creative.enhancedvisuals.EVManager;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Particle;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.sound.PositionedSound;
import team.creative.enhancedvisuals.client.sound.SoundMuteHandler;
import team.creative.enhancedvisuals.client.sound.TickedSound;
import team.creative.enhancedvisuals.common.event.EVEvents;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class EVManagerClient implements EVManager {
    
    private final Random random = new Random();
    private HashMapList<VisualCategory, Visual> visuals = new HashMapList<>();
    private List<TickedSound> playing = new ArrayList<>();
    
    public void respawn() {
        clearEverything();
    }
    
    public void clientTick() {
        if (EVClient.shouldTick()) {
            Player player = Minecraft.getInstance().player;
            onTick(player);
        }
        SoundMuteHandler.tick();
    }
    
    @Override
    public Random random() {
        return random;
    }
    
    public void onTick(@Nullable Player player) {
        boolean areEyesInWater = player != null && EVEvents.areEyesInWater(player);
        
        synchronized (visuals) {
            for (Iterator<Visual> iterator = visuals.iterator(); iterator.hasNext();) {
                Visual visual = iterator.next();
                
                int factor = 1;
                if (areEyesInWater && visual.isAffectedByWater())
                    factor = EnhancedVisuals.CONFIG.waterSubstractFactor;
                
                for (int i = 0; i < factor; i++) {
                    if (!visual.tick()) {
                        visual.removeFromDisplay();
                        iterator.remove();
                        break;
                    }
                }
            }
            
            for (VisualHandler handler : VisualRegistry.handlers()) {
                if (handler.isEnabled(player))
                    handler.tick(player);
            }
        }
        
        if (player != null && !player.isAlive())
            clearEverything();
        
        if (!playing.isEmpty())
            playing.removeIf(x -> x.isStopped());
    }
    
    @Override
    public void playSound(ResourceLocation location) {
        playSound(location, null, 1.0F);
    }
    
    @Override
    public void playSound(ResourceLocation location, BlockPos pos) {
        playSound(location, pos, 1.0F);
    }
    
    @Override
    public void playSound(ResourceLocation location, float volume) {
        playSound(location, null, volume);
    }
    
    @Override
    public void playSound(ResourceLocation location, BlockPos pos, float volume) {
        if (!EVClient.shouldRender())
            return;
        if (pos != null)
            Minecraft.getInstance().getSoundManager().play(new PositionedSound(location, SoundSource.MASTER, volume, 1, pos));
        else
            Minecraft.getInstance().getSoundManager().play(new PositionedSound(location, SoundSource.MASTER, volume, 1));
    }
    
    @Override
    public void playSoundFadeOut(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
        if (!EVClient.shouldRender())
            return;
        playTicking(location, pos, volume);
    }
    
    @Override
    public Collection<Visual> visuals(VisualCategory category) {
        return visuals.get(category);
    }
    
    @Override
    public void clearEverything() {
        synchronized (visuals) {
            visuals.removeKey(VisualCategory.particle);
        }
        if (!playing.isEmpty()) {
            for (TickedSound sound : playing)
                sound.stop();
            playing.clear();
            SoundMuteHandler.endMuting();
        }
    }
    
    @Override
    public void add(Visual visual) {
        if (!visual.type.disabled) {
            visual.addToDisplay();
            visuals.add(visual.getCategory(), visual);
        }
    }
    
    @Override
    public boolean remove(Visual visual) {
        if (visuals.removeValue(visual.getCategory(), visual)) {
            visual.removeFromDisplay();
            return true;
        }
        return false;
    }
    
    @Override
    public void playTicking(ResourceLocation location, BlockPos pos, DecimalCurve volume) {
        TickedSound sound;
        if (pos != null)
            sound = new TickedSound(location, SoundSource.MASTER, 1, pos, volume);
        else
            sound = new TickedSound(location, SoundSource.MASTER, 1, volume);
        playing.add(sound);
        Minecraft.getInstance().getSoundManager().play(sound);
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, IntMinMax time) {
        return addVisualFadeOut(vt, handler, new DecimalCurve(0, 1, time.next(random), 0));
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, int time) {
        return addVisualFadeOut(vt, handler, new DecimalCurve(0, 1, time, 0));
    }
    
    @Override
    public Visual addVisualFadeOut(VisualType vt, VisualHandler handler, Curve curve) {
        var ct = EVClient.get(vt);
        Visual v = new Visual(vt, handler, curve, ct.getVariantAmount() > 1 ? random.nextInt(ct.getVariantAmount()) : 0);
        add(v);
        return v;
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate) {
        addParticlesFadeOut(vt, handler, count, new DecimalCurve(0, 1, time.next(random), 0), rotate, null);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, IntMinMax time, boolean rotate, @Nullable Color color) {
        addParticlesFadeOut(vt, handler, count, new DecimalCurve(0, 1, time.next(random), 0), rotate, color);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, int time, boolean rotate) {
        addParticlesFadeOut(vt, handler, count, new DecimalCurve(0, 1, time, 0), rotate, null);
    }
    
    @Override
    public void addParticlesFadeOut(VisualType vt, VisualHandler handler, int count, Curve curve, boolean rotate, @Nullable Color color) {
        if (vt.disabled)
            return;
        var ct = EVClient.get(vt);
        var mc = Minecraft.getInstance();
        for (int i = 0; i < count; i++) {
            int screenWidth = mc.getWindow().getWidth();
            int screenHeight = mc.getWindow().getHeight();
            
            int width = ct.getWidth(screenWidth, screenHeight);
            int height = ct.getHeight(screenWidth, screenHeight);
            
            if (vt.scaleVariants()) {
                double scale = vt.randomScale(random);
                width *= scale;
                height *= scale;
            }
            
            Particle particle = new Particle(vt, handler, curve, generateOffset(random, screenWidth, width), generateOffset(random, screenHeight, height), width, height, vt
                    .canRotate() && rotate ? random.nextFloat() * 360 : 0, random.nextInt(ct.getVariantAmount()));
            if (color != null)
                particle.color = color;
            add(particle);
        }
    }
    
    @Override
    public Particle addParticle(VisualType vt, VisualHandler handler, boolean rotate, @Nullable Color color) {
        var mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getWidth();
        int screenHeight = mc.getWindow().getHeight();
        
        var ct = EVClient.get(vt);
        
        int width = ct.getWidth(screenWidth, screenHeight);
        int height = ct.getHeight(screenWidth, screenHeight);
        
        if (vt.scaleVariants()) {
            double scale = vt.randomScale(random);
            width *= scale;
            height *= scale;
        }
        
        Particle particle = new Particle(vt, handler, generateOffset(random, screenWidth, width), generateOffset(random, screenHeight, height), width, height, vt
                .canRotate() && rotate ? random.nextFloat() * 360 : 0, random.nextInt(ct.getVariantAmount()));
        particle.setOpacityInternal(1);
        if (color != null)
            particle.color = color;
        add(particle);
        return particle;
    }
    
    @Override
    public int generateOffset(Random rand, int dimensionLength, int spacingBuffer) {
        int half = dimensionLength / 2;
        float multiplier = (float) (1 - Math.pow(rand.nextDouble(), 2));
        float textureCenterPosition = rand.nextInt(2) == 0 ? half + half * multiplier : half - half * multiplier;
        return (int) (textureCenterPosition - (spacingBuffer / 2.0F));
    }
    
}

package team.creative.enhancedvisuals.client.sound;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ChannelManager;
import net.minecraft.client.audio.ChannelManager.Entry;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundEngine;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

@OnlyIn(value = Dist.CLIENT)
public class SoundMuteHandler {
    
    private static Minecraft mc = Minecraft.getInstance();
    
    public static boolean isMuting = false;
    
    public static SoundEngine engine;
    public static SoundHandler handler;
    
    public static ArrayList<String> ignoredSounds;
    
    public static DecimalCurve muteGraph;
    public static int timeTick = 0;
    
    public static void tick() {
        if (isMuting) {
            double factor = muteGraph.valueAt(timeTick);
            
            if (factor <= 0)
                endMuting();
            else {
                setMuteVolume((float) (1 - factor));
                timeTick++;
            }
        }
    }
    
    public static float getClampedVolume(ISound soundIn) {
        return getClampedVolume(soundIn, isMuting ? (float) (1 - muteGraph.valueAt(timeTick)) : 1);
    }
    
    private static Field playingSoundsChannelField = ObfuscationReflectionHelper.findField(SoundEngine.class, "field_217942_m");
    
    public static Map<ISound, ChannelManager.Entry> getSounds() {
        try {
            return (Map<ISound, Entry>) playingSoundsChannelField.get(engine);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setMuteVolume(float muteVolume) {
        if (!isMuting)
            return;
        
        getSounds().forEach((p_217926_1_, p_217926_2_) -> {
            float f = getClampedVolume(p_217926_1_, muteVolume);
            p_217926_2_.runOnSoundExecutor((p_217923_1_) -> {
                p_217923_1_.setGain(f);
                
            });
        });
    }
    
    private static float getClampedVolume(ISound soundIn, float muteVolume) {
        return MathHelper.clamp(soundIn.getVolume() * getVolume(soundIn.getCategory()), 0.0F, 1.0F) * muteVolume;
    }
    
    private static float getVolume(SoundCategory category) {
        return category != null && category != SoundCategory.MASTER ? mc.gameSettings.getSoundLevel(category) : 1.0F;
    }
    
    public static boolean startMuting(DecimalCurve muteGraph) {
        if (engine == null) {
            handler = Minecraft.getInstance().getSoundHandler();
            engine = ObfuscationReflectionHelper.getPrivateValue(SoundHandler.class, handler, "field_147694_f");
        }
        
        if (isMuting && SoundMuteHandler.muteGraph.valueAt(timeTick) > muteGraph.valueAt(0)) {
            SoundMuteHandler.muteGraph = muteGraph;
            SoundMuteHandler.timeTick = 0;
            tick();
            return true;
        } else if (!isMuting) {
            SoundMuteHandler.muteGraph = muteGraph;
            SoundMuteHandler.timeTick = 0;
            ignoredSounds = new ArrayList<>();
            isMuting = true;
            tick();
            return true;
        }
        
        return false;
    }
    
    public static void endMuting() {
        setMuteVolume(1F);
        isMuting = false;
        ignoredSounds = null;
    }
    
}

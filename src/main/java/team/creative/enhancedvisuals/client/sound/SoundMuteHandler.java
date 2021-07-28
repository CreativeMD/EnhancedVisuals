package team.creative.enhancedvisuals.client.sound;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.ChannelAccess.ChannelHandle;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import team.creative.creativecore.common.config.premade.curve.DecimalCurve;

@OnlyIn(value = Dist.CLIENT)
public class SoundMuteHandler {
    
    private static Method calculateVolumeMethod = ObfuscationReflectionHelper.findMethod(SoundEngine.class, "m_120327_", SoundInstance.class);
    
    public static boolean isMuting = false;
    
    public static SoundEngine engine;
    public static SoundManager manager;
    
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
    
    public static float getClampedVolume(float volume) {
        return volume * (isMuting ? (float) (1 - muteGraph.valueAt(timeTick)) : 1);
    }
    
    private static Field playingSoundsChannelField = ObfuscationReflectionHelper.findField(SoundEngine.class, "f_120226_");
    
    public static Map<SoundInstance, ChannelAccess.ChannelHandle> getSounds() {
        try {
            return (Map<SoundInstance, ChannelHandle>) playingSoundsChannelField.get(engine);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void setMuteVolume(float muteVolume) {
        if (!isMuting)
            return;
        
        getSounds().forEach((p_217926_1_, p_217926_2_) -> {
            try {
                float f = (float) calculateVolumeMethod.invoke(engine, p_217926_1_);
                p_217926_2_.execute((p_217923_1_) -> {
                    p_217923_1_.setVolume(f);
                    
                });
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
            
        });
    }
    
    public static boolean startMuting(DecimalCurve muteGraph) {
        if (engine == null) {
            manager = Minecraft.getInstance().getSoundManager();
            engine = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, manager, "f_120349_");
        }
        
        if (isMuting && SoundMuteHandler.muteGraph.valueAt(timeTick) > muteGraph.valueAt(0)) {
            SoundMuteHandler.muteGraph = muteGraph;
            SoundMuteHandler.timeTick = 0;
            tick();
            return true;
        } else if (!isMuting) {
            SoundMuteHandler.muteGraph = muteGraph;
            SoundMuteHandler.timeTick = 0;
            isMuting = true;
            tick();
            return true;
        }
        
        return false;
    }
    
    public static void endMuting() {
        setMuteVolume(1F);
        isMuting = false;
    }
    
}

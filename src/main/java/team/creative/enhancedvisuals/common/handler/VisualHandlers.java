package team.creative.enhancedvisuals.common.handler;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class VisualHandlers {
    
    public static ExplosionHandler EXPLOSION;
    public static PotionHandler POTION;
    
    public static SandSplatHandler SAND;
    public static SplashHandler SPLASH;
    public static DamageHandler DAMAGE;
    
    public static SlenderHandler SLENDER;
    public static SaturationHandler SATURATION;
    public static HeartbeatHandler HEARTBEAT;
    
    public static UnderwaterHandler UNDERWATER;
    public static RainHandler RAIN;
    
    public static void init() {
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "explosion"), EXPLOSION = new ExplosionHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "potion"), POTION = new PotionHandler());
        
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "sand"), SAND = new SandSplatHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "splash"), SPLASH = new SplashHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "damage"), DAMAGE = new DamageHandler());
        
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "slender"), SLENDER = new SlenderHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "saturation"), SATURATION = new SaturationHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "heartbeat"), HEARTBEAT = new HeartbeatHandler());
        
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "underwater"), UNDERWATER = new UnderwaterHandler());
        VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "rain"), RAIN = new RainHandler());
    }
    
}

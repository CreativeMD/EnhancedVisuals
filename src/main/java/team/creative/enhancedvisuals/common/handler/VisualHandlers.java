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
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "explosion"), EXPLOSION = new ExplosionHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "potion"), POTION = new PotionHandler());
        
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "sand"), SAND = new SandSplatHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "splash"), SPLASH = new SplashHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "damage"), DAMAGE = new DamageHandler());
        
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "slender"), SLENDER = new SlenderHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "saturation"), SATURATION = new SaturationHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "heartbeat"), HEARTBEAT = new HeartbeatHandler());
        
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "underwater"), UNDERWATER = new UnderwaterHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "rain"), RAIN = new RainHandler());
    }
    
}

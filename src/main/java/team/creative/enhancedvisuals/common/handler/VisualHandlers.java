package team.creative.enhancedvisuals.common.handler;

import net.minecraft.resources.Identifier;
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
    
    public static HealthHandler HEALTH;
    
    public static void init() {
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "explosion"), EXPLOSION = new ExplosionHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "potion"), POTION = new PotionHandler());
        
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "sand"), SAND = new SandSplatHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "splash"), SPLASH = new SplashHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "damage"), DAMAGE = new DamageHandler());
        
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "slender"), SLENDER = new SlenderHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "saturation"), SATURATION = new SaturationHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "heartbeat"), HEARTBEAT = new HeartbeatHandler());
        
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "underwater"), UNDERWATER = new UnderwaterHandler());
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "rain"), RAIN = new RainHandler());
        
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "health"), HEALTH = new HealthHandler());
    }
    
}

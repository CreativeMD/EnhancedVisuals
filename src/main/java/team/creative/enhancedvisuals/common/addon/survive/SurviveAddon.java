package team.creative.enhancedvisuals.common.addon.survive;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class SurviveAddon {
    
    public static ThirstHandler thirst;
    public static TemperatureHandler temperature;
    
    public static void load() {
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "thirst"), thirst = new ThirstHandler());
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "temperature"), temperature = new TemperatureHandler());
    }
    
}

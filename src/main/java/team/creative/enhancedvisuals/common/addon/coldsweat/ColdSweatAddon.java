package team.creative.enhancedvisuals.common.addon.coldsweat;

import net.minecraft.resources.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class ColdSweatAddon {
    
    public static TemperatureHandler temperature;
    
    public static void load() {
        VisualRegistry.registerHandler(ResourceLocation.tryBuild(EnhancedVisuals.MODID, "temperature"), temperature = new TemperatureHandler());
    }
    
}

package team.creative.enhancedvisuals.common.addon.coldsweat;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class ColdSweatAddon {
    
    public static TemperatureHandler temperature;
    
    public static void load() {
        VisualRegistry.registerHandler(Identifier.tryBuild(EnhancedVisuals.MODID, "temperature"), temperature = new TemperatureHandler());
    }
    
}

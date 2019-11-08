package team.creative.enhancedvisuals.common.handler;

import net.minecraft.util.ResourceLocation;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

public class VisualHandlers {
	
	public static ExplosionHandler EXPLOSION;
	
	public static void init() {
		VisualRegistry.registerHandler(new ResourceLocation(EnhancedVisuals.MODID, "explosion"), EXPLOSION = new ExplosionHandler());
	}
	
}

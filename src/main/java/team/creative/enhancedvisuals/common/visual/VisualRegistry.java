package team.creative.enhancedvisuals.common.visual;

import java.util.Collection;
import java.util.LinkedHashMap;

import net.minecraft.util.ResourceLocation;
import team.creative.enhancedvisuals.api.VisualHandler;

public class VisualRegistry {
	
	private static LinkedHashMap<ResourceLocation, VisualHandler> handlers = new LinkedHashMap<>();
	
	public static void registerHandler(ResourceLocation location, VisualHandler handler) {
		handlers.put(location, handler);
	}
	
	public static Collection<VisualHandler> handlers() {
		return handlers.values();
	}
	
}

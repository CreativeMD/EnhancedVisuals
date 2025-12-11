package team.creative.enhancedvisuals.common.visual;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.resources.Identifier;
import team.creative.enhancedvisuals.api.VisualHandler;

public class VisualRegistry {
    
    private static LinkedHashMap<Identifier, VisualHandler> handlers = new LinkedHashMap<>();
    
    public static void registerHandler(Identifier identifier, VisualHandler handler) {
        handlers.put(identifier, handler);
    }
    
    public static Collection<VisualHandler> handlers() {
        return handlers.values();
    }
    
    public static Set<Entry<Identifier, VisualHandler>> entrySet() {
        return handlers.entrySet();
    }
    
}

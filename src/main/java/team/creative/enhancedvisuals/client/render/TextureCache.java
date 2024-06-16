package team.creative.enhancedvisuals.client.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public abstract class TextureCache {
    
    public abstract ResourceLocation getFirst();
    
    public abstract ResourceLocation getResource();
    
    public static TextureCache parse(ResourceManager manager, String domain, String baseLocation) {
        ResourceLocation location = null;
        int i = 0;
        Resource resource = null;
        List<ResourceLocation> locations = null;
        while ((resource = manager.getResource(location = ResourceLocation.tryBuild(domain, baseLocation + "-" + i + ".png")).orElse(null)) != null) {
            if (locations == null)
                locations = new ArrayList<>();
            locations.add(location);
            i++;
        }
        
        if (locations != null) {
            int animationSpeed = 1;
            try {
                resource = manager.getResource(ResourceLocation.tryBuild(domain, baseLocation + ".ani")).orElse(null);
                if (resource != null) {
                    try {
                        InputStream input = resource.open();
                        try {
                            animationSpeed = Integer.parseInt(new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n")));
                        } finally {
                            input.close();
                        }
                    } catch (NumberFormatException e) {}
                    
                }
            } catch (IOException e) {}
            
            return new TextureCacheAnimation(locations.toArray(new ResourceLocation[locations.size()]), animationSpeed);
        }
        
        if (manager.getResource(location = ResourceLocation.tryBuild(domain, baseLocation + ".png")).orElse(null) != null)
            return new TextureCacheSimple(location);
        return null;
    }
    
}

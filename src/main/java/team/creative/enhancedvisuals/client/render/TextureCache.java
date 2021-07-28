package team.creative.enhancedvisuals.client.render;

import java.io.BufferedReader;
import java.io.IOException;
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
        try {
            ResourceLocation location = null;
            int i = 0;
            Resource resource = null;
            List<ResourceLocation> locations = null;
            try {
                while ((resource = manager.getResource(location = new ResourceLocation(domain, baseLocation + "-" + i + ".png"))) != null) {
                    if (locations == null)
                        locations = new ArrayList<>();
                    locations.add(location);
                    i++;
                }
                
            } catch (IOException e) {}
            
            if (locations != null) {
                int animationSpeed = 1;
                try {
                    resource = manager.getResource(new ResourceLocation(domain, baseLocation + ".ani"));
                    if (resource != null) {
                        try {
                            animationSpeed = Integer.parseInt(new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()
                                    .collect(Collectors.joining("\n")));
                        } catch (NumberFormatException e) {}
                        
                    }
                } catch (IOException e) {}
                
                return new TextureCacheAnimation(locations.toArray(new ResourceLocation[locations.size()]), animationSpeed);
            }
            
            if (manager.getResource(location = new ResourceLocation(domain, baseLocation + ".png")) != null)
                return new TextureCacheSimple(location);
        } catch (IOException e) {}
        return null;
    }
    
}

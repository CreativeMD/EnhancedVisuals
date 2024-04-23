package team.creative.enhancedvisuals.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@Environment(EnvType.CLIENT)
@OnlyIn(Dist.CLIENT)
public class EVClient {
    
    private static Minecraft mc = Minecraft.getInstance();
    
    public static void init() {
        ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
        
        reloadableResourceManager.registerReloadListener(new SimplePreparableReloadListener() {
            
            @Override
            protected Object prepare(ResourceManager manager, ProfilerFiller profiler) {
                return null;
            }
            
            @Override
            protected void apply(Object object, ResourceManager manager, ProfilerFiller profiler) {
                VisualManager.clearEverything();
                EVRenderer.reloadResources = true;
            }
        });
        
        ResourceManager manager = mc.getResourceManager();
        for (VisualType type : VisualType.getTypes())
            type.loadResources(manager);
    }
    
    public static boolean shouldRender() {
        return mc.player != null ? (!mc.player.isSpectator() && (!mc.player.isCreative() || EnhancedVisuals.CONFIG.doEffectsInCreative)) : true;
    }
    
    public static boolean shouldTick() {
        return true;
    }
}

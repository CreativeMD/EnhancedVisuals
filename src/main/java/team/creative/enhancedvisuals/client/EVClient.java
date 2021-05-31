package team.creative.enhancedvisuals.client;

import java.util.function.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.ISelectiveResourceReloadListener;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@OnlyIn(value = Dist.CLIENT)
public class EVClient {
    
    private static Minecraft mc = Minecraft.getInstance();
    
    public static void init(FMLClientSetupEvent event) {
        IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) event.getMinecraftSupplier().get().getResourceManager();
        
        reloadableResourceManager.registerReloadListener(new ISelectiveResourceReloadListener() {
            @Override
            public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate) {
                VisualManager.clearParticles();
                
                EVRenderer.reloadResources = true;
            }
        });
        
        IResourceManager manager = mc.getResourceManager();
        for (VisualType type : VisualType.getTypes()) {
            type.loadResources(manager);
        }
        
        MinecraftForge.EVENT_BUS.register(EVRenderer.class);
    }
    
    public static boolean shouldRender() {
        return mc.player != null ? (!mc.player.isSpectator() && (!mc.player.isCreative() || EnhancedVisuals.CONFIG.doEffectsInCreative)) : true;
    }
    
    public static boolean shouldTick() {
        return true;
    }
}

package team.creative.enhancedvisuals.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.render.EVRenderer;

@OnlyIn(value = Dist.CLIENT)
public class EVClient {
    
    private static Minecraft mc = Minecraft.getInstance();
    
    public static void init(FMLClientSetupEvent event) {
        ReloadableResourceManager reloadableResourceManager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
        
        reloadableResourceManager.registerReloadListener(new PreparableReloadListener() {
            
            @Override
            public CompletableFuture<Void> reload(PreparationBarrier p_10638_, ResourceManager p_10639_, ProfilerFiller p_10640_, ProfilerFiller p_10641_, Executor p_10642_, Executor p_10643_) {
                return CompletableFuture.runAsync(() -> {
                    VisualManager.clearParticles();
                    EVRenderer.reloadResources = true;
                }, p_10643_);
            }
        });
        
        ResourceManager manager = mc.getResourceManager();
        for (VisualType type : VisualType.getTypes())
            type.loadResources(manager);
        
        MinecraftForge.EVENT_BUS.register(EVRenderer.class);
    }
    
    public static boolean shouldRender() {
        return mc.player != null ? (!mc.player.isSpectator() && (!mc.player.isCreative() || EnhancedVisuals.CONFIG.doEffectsInCreative)) : true;
    }
    
    public static boolean shouldTick() {
        return true;
    }
}

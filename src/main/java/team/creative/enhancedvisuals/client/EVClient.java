package team.creative.enhancedvisuals.client;

import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
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
        
        reloadableResourceManager.registerReloadListener(new SimplePreparableReloadListener() {
            
            @Override
            protected Object prepare(ResourceManager p_10796_, ProfilerFiller p_10797_) {
                return null;
            }
            
            @Override
            protected void apply(Object p_10793_, ResourceManager p_10794_, ProfilerFiller p_10795_) {
                VisualManager.clearParticles();
                EVRenderer.reloadResources = true;
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

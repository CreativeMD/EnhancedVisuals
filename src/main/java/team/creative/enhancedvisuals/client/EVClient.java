package team.creative.enhancedvisuals.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.render.EVRenderer;
import team.creative.enhancedvisuals.server.EVServer;

@SideOnly(Side.CLIENT)
public class EVClient extends EVServer {
    
    private static Minecraft mc = Minecraft.getMinecraft();
    
    @Override
    public void load(FMLInitializationEvent event) {
        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {
            
            @Override
            public void onResourceManagerReload(IResourceManager resourceManager) {
                VisualManager.clearParticles();
                
                for (VisualType type : VisualType.getTypes()) {
                    type.loadResources(resourceManager);
                }
            }
        });
        
        IResourceManager manager = mc.getResourceManager();
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

package team.creative.enhancedvisuals.client;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import net.minecraft.client.Minecraft;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IFutureReloadListener;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
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
		((IReloadableResourceManager) event.getMinecraftSupplier().get().getResourceManager()).addReloadListener(new IFutureReloadListener() {
			
			@Override
			public CompletableFuture<Void> reload(IStage arg0, IResourceManager resourceManager, IProfiler arg2, IProfiler arg3, Executor arg4, Executor arg5) {
				CompletableFuture future = CompletableFuture.runAsync(new Runnable() {
					
					@Override
					public void run() {
						VisualManager.clearVisuals();
						
						for (VisualType type : VisualType.getTypes()) {
							type.loadResources(resourceManager);
						}
					}
				}, arg4);
				
				return future;
			}
		});
		
		MinecraftForge.EVENT_BUS.register(EVRenderer.class);
	}
	
	public static boolean shouldRender() {
		return mc.player != null ? (!mc.player.isCreative() || !EnhancedVisuals.CONFIG.doEffectsInCreative) : true;
	}
	
	public static boolean shouldTick() {
		return true;
	}
}

package com.sonicjumper.enhancedvisuals;

import com.sonicjumper.enhancedvisuals.events.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class EnhancedVisualsClient extends EnhancedVisualsServer {
	
	@Override
	public void init(FMLLoadCompleteEvent event) {
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(new IResourceManagerReloadListener() {
			
			@Override
			public void onResourceManagerReload(IResourceManager resourceManager) {
				VisualManager.clearAllVisuals();
				
				for (int i = 0; i < VisualCategory.values().length; i++) {
					VisualCategory category = VisualCategory.values()[i];
					for (int j = 0; j < category.types.size(); j++) {
						category.types.get(j).loadTextures(resourceManager);
						VisualPersistent persistent = category.types.get(j).createPersitentVisual();
						if (persistent != null)
							VisualManager.addPersistentVisual(persistent);
					}
				}
			}
		});
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	@Override
	public void load(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(VisualEventHandler.class);
	}
	
}

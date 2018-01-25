package com.sonicjumper.enhancedvisuals;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sonicjumper.enhancedvisuals.addon.igcm.IGCMLoader;
import com.sonicjumper.enhancedvisuals.death.DeathMessages;
import com.sonicjumper.enhancedvisuals.events.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.handlers.VisualHandler;
import com.sonicjumper.enhancedvisuals.visuals.VisualPersistent;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualCategory;
import com.sonicjumper.enhancedvisuals.visuals.types.VisualType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = EnhancedVisuals.modid, name = EnhancedVisuals.name, acceptedMinecraftVersions = "")
public class EnhancedVisuals {
	
	public static final String modid = "enhancedvisuals";
	public static final String name = "Enhanced Visuals";
	public static final String version = "1.2.0";
	
	public static Logger log = LogManager.getLogger(modid);
	
	@SidedProxy(clientSide = "com.sonicjumper.enhancedvisuals.EnhancedVisualsClient", serverSide = "com.sonicjumper.enhancedvisuals.EnhancedVisualsServer")
	public static EnhancedVisualsServer proxy;
	
	@EventHandler
	public void init(FMLLoadCompleteEvent event) {
		proxy.init(event);
	}
	
	public static boolean noEffectsForCreative = false;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.preInit(event);
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		EnhancedVisuals.noEffectsForCreative = config.getBoolean("noEffectsForCreative", "general", EnhancedVisuals.noEffectsForCreative, "If players in creative mod should have effects");
		
		VisualType.onLoad();
		for (int i = 0; i < VisualCategory.values().length; i++) {
			VisualCategory category = VisualCategory.values()[i];
			for (int j = 0; j < category.types.size(); j++) {
				category.types.get(j).initConfig(config);
			}
		}
		
		for (Iterator<VisualHandler> iterator = VisualHandler.getAllHandlers().iterator(); iterator.hasNext();) {
			VisualHandler handler = iterator.next();
			handler.initConfig(config);
		}
		
		VisualHandler.afterInit();
		
		DeathMessages.loadConfig(config);
		config.save();
		
		//Init IGCM
		if(Loader.isModLoaded("igcm"))
			IGCMLoader.initConfiguration();
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.load(event);
		
	}
	
}

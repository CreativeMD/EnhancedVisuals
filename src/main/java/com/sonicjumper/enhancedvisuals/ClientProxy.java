package com.sonicjumper.enhancedvisuals;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;

public class ClientProxy extends ServerProxy {
	public void registerThings(FMLPreInitializationEvent event)
	{
		baseJarPath = event.getSourceFile().getPath();
		visualsDirectory = event.getSourceFile().getPath() + "/assets/" + Base.MODID + "/DefaultTheme/visuals/";
		//soundsDirectory = event.getSourceFile().getPath() + "/assets/" + Base.MODID + "/DefaultTheme/sounds/";
		// Normal event registers
		//MinecraftForge.EVENT_BUS.register(Base.instance.renderer);
		//MinecraftForge.EVENT_BUS.register(Base.instance.eventHandler);
		// Tick registers
		//FMLCommonHandler.instance().bus().register(Base.instance.eventHandler);
		//FMLCommonHandler.instance().bus().register(Base.instance.renderer);
	}

	public static String getVisualsDirectory(String themePack)
	{
		return baseJarPath + "/assets/" + Base.MODID + "/" + themePack.toLowerCase() + "/visuals/";
		//return "/assets/" + Base.MODID + "/" + themePack + "/visuals/";
	}
}

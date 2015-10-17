package com.sonicjumper.enhancedvisuals;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy {
	public static String baseJarPath;
	public static String visualsDirectory;
	public static String soundsDirectory;

	public void registerThings(FMLPreInitializationEvent event)
	{
		//MinecraftForge.EVENT_BUS.register(Base.instance.eventHandler);
		//FMLCommonHandler.instance().bus().register(Base.instance.eventHandler);
	}
}

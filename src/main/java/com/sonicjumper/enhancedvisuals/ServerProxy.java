package com.sonicjumper.enhancedvisuals;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

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

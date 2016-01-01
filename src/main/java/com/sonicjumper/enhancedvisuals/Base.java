package com.sonicjumper.enhancedvisuals;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.event.VisualRenderer;
import com.sonicjumper.enhancedvisuals.shaders.ShaderHelper;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = Base.MODID, name = Base.MODNAME, version = Base.MODVER)
public class Base {
	public static final String MODID = "sonicvisuals";
	public static final String MODNAME = "Enhanced Visuals";
	public static final String MODVER = "0.3";
	
	@Instance(value = Base.MODID)
	public static Base instance;
	
	public VisualManager manager;
	
	@SideOnly(Side.CLIENT)
	public VisualRenderer renderer;
	public VisualEventHandler eventHandler;
	// SoundHandler removes because of new .json files
	
	public ShaderHelper shaderHelper;
	
	@SidedProxy(clientSide = "com.sonicjumper.enhancedvisuals.ClientProxy", serverSide = "com.sonicjumper.enhancedvisuals.ServerProxy")
	public static ServerProxy proxy;
	
	public static Logger log;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		log = event.getModLog();
		
		ConfigCore cc = new ConfigCore(event.getSuggestedConfigurationFile());
		cc.loadConfig();
		
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			this.renderer = new VisualRenderer();
		
		this.eventHandler = new VisualEventHandler();
		
		this.shaderHelper = new ShaderHelper(Minecraft.getMinecraft(), Minecraft.getMinecraft().getResourceManager());
		
		proxy.registerThings(event);
		this.manager = new VisualManager();
		
		try
		{
			Visual initializer = new Visual(VisualType.splatter, 0);
			System.out.println("[EnhancedVisuals] Looks like everything loaded nicely! Enjoy the mod!");
		} catch (NullPointerException e) {
			System.out.println("Could not load Visuals");
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
		{
			MinecraftForge.EVENT_BUS.register(Base.instance.renderer);
			FMLCommonHandler.instance().bus().register(Base.instance.renderer);
		}
		
		MinecraftForge.EVENT_BUS.register(Base.instance.eventHandler);
		FMLCommonHandler.instance().bus().register(Base.instance.eventHandler);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}

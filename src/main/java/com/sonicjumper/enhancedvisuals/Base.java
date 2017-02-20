package com.sonicjumper.enhancedvisuals;

import org.apache.logging.log4j.Logger;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.sonicjumper.enhancedvisuals.event.VisualEventHandler;
import com.sonicjumper.enhancedvisuals.event.VisualRenderer;
import com.sonicjumper.enhancedvisuals.shaders.ShaderHelper;
import com.sonicjumper.enhancedvisuals.visuals.Visual;
import com.sonicjumper.enhancedvisuals.visuals.VisualManager;
import com.sonicjumper.enhancedvisuals.visuals.VisualType;

@Mod(modid = Base.MODID, name = Base.MODNAME, version = Base.MODVER,acceptedMinecraftVersions="",clientSideOnly = true)
public class Base {
	public static final String MODID = "enhancedvisuals";
	public static final String MODNAME = "Enhanced Visuals";
	public static final String MODVER = "1.0";
	
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
		
		proxy.registerThings(event);
		
		ConfigCore cc = new ConfigCore(event.getSuggestedConfigurationFile());
		cc.loadConfig();
		
		this.renderer = new VisualRenderer();
		
		this.eventHandler = new VisualEventHandler();
		
		this.shaderHelper = new ShaderHelper(Minecraft.getMinecraft(), Minecraft.getMinecraft().getResourceManager());
		
		
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
		MinecraftForge.EVENT_BUS.register(Base.instance.renderer);
		
		MinecraftForge.EVENT_BUS.register(Base.instance.eventHandler);
		//FMLCommonHandler.instance().bus().register(Base.instance.eventHandler);
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
}

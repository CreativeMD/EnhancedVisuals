package team.creative.enhancedvisuals;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.creativemd.creativecore.common.config.holder.ConfigHolderDynamic;
import com.creativemd.creativecore.common.config.holder.CreativeConfigRegistry;
import com.creativemd.creativecore.common.config.sync.ConfigSynchronization;
import com.creativemd.creativecore.common.packet.CreativeCorePacket;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.common.addon.simpledifficulty.SimpleDifficultyAddon;
import team.creative.enhancedvisuals.common.addon.toughasnails.ToughAsNailsAddon;
import team.creative.enhancedvisuals.common.death.DeathMessages;
import team.creative.enhancedvisuals.common.event.EVEvents;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;
import team.creative.enhancedvisuals.server.EVServer;

@Mod(modid = EnhancedVisuals.MODID, name = EnhancedVisuals.NAME, version = EnhancedVisuals.VERSION, acceptedMinecraftVersions = "", dependencies = "required-before:creativecore", guiFactory = "team.creative.enhancedvisuals.client.EVSettings")
public class EnhancedVisuals {
	
	public static final String MODID = "enhancedvisuals";
	public static final String NAME = "Enhanced Visuals";
	public static final String VERSION = "1.3.0";
	
	public static final Logger LOGGER = LogManager.getLogger(EnhancedVisuals.MODID);
	public static EVEvents EVENTS;
	public static DeathMessages MESSAGES;
	public static EnhancedVisualsConfig CONFIG;
	
	@SidedProxy(clientSide = "team.creative.enhancedvisuals.client.EVClient", serverSide = "team.creative.enhancedvisuals.server.EVServer")
	public static EVServer proxy;
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		CreativeCorePacket.registerPacket(ExplosionPacket.class);
		CreativeCorePacket.registerPacket(DamagePacket.class);
		CreativeCorePacket.registerPacket(PotionPacket.class);
		
		MinecraftForge.EVENT_BUS.register(EVENTS = new EVEvents());
		
		VisualHandlers.init();
		MESSAGES = new DeathMessages();
		CONFIG = new EnhancedVisualsConfig();
		
		if (Loader.isModLoaded("toughasnails"))
			ToughAsNailsAddon.load();
		
		if (Loader.isModLoaded("simpledifficulty"))
			SimpleDifficultyAddon.load();
		
		proxy.load(event);
		
		ConfigHolderDynamic root = CreativeConfigRegistry.ROOT.registerFolder(MODID);
		root.registerValue("general", CONFIG, ConfigSynchronization.CLIENT, false);
		root.registerValue("messages", MESSAGES);
		ConfigHolderDynamic handlers = root.registerFolder("handlers", ConfigSynchronization.CLIENT);
		for (Entry<ResourceLocation, VisualHandler> entry : VisualRegistry.entrySet())
			handlers.registerValue(entry.getKey().getResourcePath(), entry.getValue());
		
	}
	
}

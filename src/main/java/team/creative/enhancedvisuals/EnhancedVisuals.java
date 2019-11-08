package team.creative.enhancedvisuals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.creative.creativecore.common.network.CreativeNetwork;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.common.death.DeathMessages;
import team.creative.enhancedvisuals.common.event.EVEvents;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;

@Mod(value = EnhancedVisuals.MODID)
public class EnhancedVisuals {
	
	public static final String MODID = "enhancedvisuals";
	
	public static final Logger LOGGER = LogManager.getLogger();
	public static CreativeNetwork NETWORK;
	public static EVEvents EVENTS;
	public static DeathMessages MESSAGES;
	public static EnhancedVisualsConfig CONFIG;
	
	public EnhancedVisuals() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}
	
	private void client(final FMLClientSetupEvent event) {
		EVClient.init(event);
	}
	
	private void init(final FMLCommonSetupEvent event) {
		NETWORK = new CreativeNetwork("1.0", LOGGER, new ResourceLocation(EnhancedVisuals.MODID, "main"));
		NETWORK.registerType(ExplosionPacket.class);
		
		MinecraftForge.EVENT_BUS.register(EVENTS = new EVEvents());
		
		VisualHandlers.init();
		MESSAGES = new DeathMessages();
		CONFIG = new EnhancedVisualsConfig();
	}
	
}

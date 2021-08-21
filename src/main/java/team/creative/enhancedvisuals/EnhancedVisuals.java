package team.creative.enhancedvisuals;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.common.config.holder.ConfigHolderDynamic;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;
import team.creative.creativecore.common.network.CreativeNetwork;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.common.addon.survive.SurviveAddon;
import team.creative.enhancedvisuals.common.death.DeathMessages;
import team.creative.enhancedvisuals.common.event.EVEvents;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

@Mod(value = EnhancedVisuals.MODID)
public class EnhancedVisuals {
    
    public static final String MODID = "enhancedvisuals";
    
    public static final Logger LOGGER = LogManager.getLogger(EnhancedVisuals.MODID);
    public static final CreativeNetwork NETWORK = new CreativeNetwork("1.0", LOGGER, new ResourceLocation(EnhancedVisuals.MODID, "main"));
    public static EVEvents EVENTS;
    public static DeathMessages MESSAGES;
    public static EnhancedVisualsConfig CONFIG;
    
    public EnhancedVisuals() {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::client));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
    }
    
    @OnlyIn(value = Dist.CLIENT)
    private void client(final FMLClientSetupEvent event) {
        EVClient.init(event);
        CreativeCoreClient.registerClientConfig(MODID);
    }
    
    private void init(final FMLCommonSetupEvent event) {
        NETWORK.registerType(ExplosionPacket.class, ExplosionPacket::new);
        NETWORK.registerType(DamagePacket.class, DamagePacket::new);
        NETWORK.registerType(PotionPacket.class, PotionPacket::new);
        
        MinecraftForge.EVENT_BUS.register(EVENTS = new EVEvents());
        
        VisualHandlers.init();
        MESSAGES = new DeathMessages();
        if (ModList.get().isLoaded("survive"))
            SurviveAddon.load();
        
        ConfigHolderDynamic root = CreativeConfigRegistry.ROOT.registerFolder(MODID);
        root.registerValue("general", CONFIG = new EnhancedVisualsConfig(), ConfigSynchronization.CLIENT, false);
        root.registerValue("messages", MESSAGES);
        ConfigHolderDynamic handlers = root.registerFolder("handlers", ConfigSynchronization.CLIENT);
        for (Entry<ResourceLocation, VisualHandler> entry : VisualRegistry.entrySet())
            handlers.registerValue(entry.getKey().getPath(), entry.getValue());
    }
    
}

package team.creative.enhancedvisuals;

import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.Mod;
import team.creative.creativecore.CreativeCore;
import team.creative.creativecore.ICreativeLoader;
import team.creative.creativecore.client.ClientLoader;
import team.creative.creativecore.client.CreativeCoreClient;
import team.creative.creativecore.common.CommonLoader;
import team.creative.creativecore.common.config.holder.ConfigHolderDynamic;
import team.creative.creativecore.common.config.holder.CreativeConfigRegistry;
import team.creative.creativecore.common.config.sync.ConfigSynchronization;
import team.creative.creativecore.common.network.CreativeNetwork;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.render.EVRenderer;
import team.creative.enhancedvisuals.common.addon.survive.SurviveAddon;
import team.creative.enhancedvisuals.common.addon.toughasnails.TANAddon;
import team.creative.enhancedvisuals.common.death.DeathMessages;
import team.creative.enhancedvisuals.common.event.EVEvents;
import team.creative.enhancedvisuals.common.handler.VisualHandlers;
import team.creative.enhancedvisuals.common.packet.DamagePacket;
import team.creative.enhancedvisuals.common.packet.ExplosionPacket;
import team.creative.enhancedvisuals.common.packet.PotionPacket;
import team.creative.enhancedvisuals.common.visual.VisualRegistry;

@Mod(value = EnhancedVisuals.MODID)
public class EnhancedVisuals implements CommonLoader, ClientLoader {
    
    public static final String MODID = "enhancedvisuals";
    
    public static final Logger LOGGER = LogManager.getLogger(EnhancedVisuals.MODID);
    public static final CreativeNetwork NETWORK = new CreativeNetwork(1, LOGGER, ResourceLocation.tryBuild(EnhancedVisuals.MODID, "main"));
    public static EVEvents EVENTS;
    public static DeathMessages MESSAGES;
    public static EnhancedVisualsConfig CONFIG;
    
    public EnhancedVisuals() {
        ICreativeLoader loader = CreativeCore.loader();
        loader.register(this);
        loader.registerClient(this);
    }
    
    @Override
    @Environment(EnvType.CLIENT)
    @OnlyIn(Dist.CLIENT)
    public void onInitializeClient() {
        CreativeCore.loader().registerClientStarted(EVClient::init);
        CreativeCore.loader().registerClientTick(() -> EVENTS.clientTick());
        CreativeCore.loader().registerClientRenderGui(EVRenderer::render);
        CreativeCoreClient.registerClientConfig(MODID);
    }
    
    @Override
    public void onInitialize() {
        NETWORK.registerType(ExplosionPacket.class, ExplosionPacket::new);
        NETWORK.registerType(DamagePacket.class, DamagePacket::new);
        NETWORK.registerType(PotionPacket.class, PotionPacket::new);
        
        ICreativeLoader loader = CreativeCore.loader();
        
        EVENTS = new EVEvents();
        
        VisualHandlers.init();
        MESSAGES = new DeathMessages();
        if (loader.isModLoaded("survive"))
            SurviveAddon.load();
        
        if (loader.isModLoaded("toughasnails"))
            TANAddon.load();
        
        ConfigHolderDynamic root = CreativeConfigRegistry.ROOT.registerFolder(MODID);
        root.registerValue("general", CONFIG = new EnhancedVisualsConfig(), ConfigSynchronization.CLIENT, false);
        root.registerValue("messages", MESSAGES);
        ConfigHolderDynamic handlers = root.registerFolder("handlers", ConfigSynchronization.CLIENT);
        for (Entry<ResourceLocation, VisualHandler> entry : VisualRegistry.entrySet())
            handlers.registerValue(entry.getKey().getPath(), entry.getValue());
    }
    
}

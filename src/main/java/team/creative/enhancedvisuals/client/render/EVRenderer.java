package team.creative.enhancedvisuals.client.render;

import java.util.Collection;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.DeathScreen;
import team.creative.creativecore.common.util.mc.LanguageUtils;
import team.creative.enhancedvisuals.EnhancedVisuals;
import team.creative.enhancedvisuals.api.Particle;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualCategory;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.client.EVClient;
import team.creative.enhancedvisuals.client.type.VisualTypeClient;

public class EVRenderer {
    
    private static String lastRenderedMessage;
    
    private static int framebufferWidth;
    private static int framebufferHeight;
    
    public static boolean reloadResources = false;
    
    public static void init() {}
    
    public static void renderShaders(DeltaTracker tracker) {
        float partialTicks = tracker.getGameTimeDeltaPartialTick(false);
        
        var mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getWidth();
        int screenHeight = mc.getWindow().getHeight();
        var renderTarget = mc.getMainRenderTarget();
        
        RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(renderTarget.getDepthTexture(), 1.0);
        renderVisuals(null, EnhancedVisuals.MANAGER.visuals(VisualCategory.shader), screenWidth, screenHeight, partialTicks);
    }
    
    public static void render(Object object) {
        GuiGraphicsExtractor graphics = (GuiGraphicsExtractor) object;
        if (EVClient.shouldRender()) {
            var mc = Minecraft.getInstance();
            
            if (reloadResources) {
                for (VisualType type : VisualType.types())
                    ((VisualTypeClient) type.clientSideType).loadResources(mc.getResourceManager());
                reloadResources = false;
            }
            
            if (!(mc.screen instanceof DeathScreen)) {
                float partialTicks = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false);
                
                if (mc.getMainRenderTarget().width != framebufferWidth || mc.getMainRenderTarget().height != framebufferHeight) {
                    for (VisualType type : VisualType.types())
                        ((VisualTypeClient) type.clientSideType).resize(mc.getMainRenderTarget());
                    framebufferWidth = mc.getMainRenderTarget().width;
                    framebufferHeight = mc.getMainRenderTarget().height;
                }
                
                int screenWidth = mc.getWindow().getGuiScaledWidth();
                int screenHeight = mc.getWindow().getGuiScaledHeight();
                
                renderVisuals(graphics, EnhancedVisuals.MANAGER.visuals(VisualCategory.overlay), screenWidth, screenHeight, partialTicks);
                renderVisuals(graphics, EnhancedVisuals.MANAGER.visuals(VisualCategory.particle), screenWidth, screenHeight, partialTicks);
                
            } else {
                if (EnhancedVisuals.MESSAGES.enabled) {
                    if (lastRenderedMessage == null)
                        lastRenderedMessage = LanguageUtils.translate(EnhancedVisuals.MESSAGES.pickRandomDeathMessage());
                    
                    if (lastRenderedMessage != null)
                        graphics.text(mc.font, "\"" + lastRenderedMessage + "\"", mc.screen.width / 2 - mc.font.width(lastRenderedMessage) / 2, 114, 16777215);
                }
            }
        }
    }
    
    public static void render(Visual visual, GuiGraphicsExtractor graphics, int screenWidth, int screenHeight, float partialTicks) {
        var ct = EVClient.get(visual.type);
        if (visual instanceof Particle p) {
            var stack = graphics.pose();
            stack.pushMatrix();
            stack.translate(p.x + p.width / 2, p.y + p.height / 2);
            stack.rotate((float) Math.toRadians(p.rotation));
            ct.render(graphics, visual.handler, visual, screenWidth, screenHeight, partialTicks);
            stack.popMatrix();
        } else
            ct.render(graphics, visual.handler, visual, screenWidth, screenHeight, partialTicks);
    }
    
    private static void renderVisuals(GuiGraphicsExtractor graphics, Collection<Visual> visuals, int screenWidth, int screenHeight, float partialTicks) {
        if (visuals == null || visuals.isEmpty())
            return;
        try {
            
            for (Visual visual : visuals)
                if (visual.isVisible())
                    render(visual, graphics, screenWidth, screenHeight, partialTicks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

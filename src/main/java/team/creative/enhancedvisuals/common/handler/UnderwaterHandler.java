package team.creative.enhancedvisuals.common.handler;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeBlur;
import team.creative.enhancedvisuals.client.VisualManager;
import team.creative.enhancedvisuals.common.event.EVEvents;

public class UnderwaterHandler extends VisualHandler {
    
    @CreativeConfig
    public double intensity = 5;
    
    @CreativeConfig
    public double fadeFactor = 0.5;
    
    @CreativeConfig
    public VisualType blur = new VisualTypeBlur("blur");
    
    public Visual blurVisual;
    
    public UnderwaterHandler() {
        enabled = false;
    }
    
    @Override
    public void tick(@Nullable Player player) {
        if (blurVisual == null) {
            blurVisual = new Visual(blur, this, 0);
            blurVisual.setOpacityInternal(0);
            VisualManager.add(blurVisual);
        }
        
        if (player != null) {
            double aimed = EVEvents.areEyesInWater(player) ? intensity : 0;
            
            if (blurVisual.getOpacityInternal() < aimed)
                blurVisual.setOpacityInternal((float) Math.min(blurVisual.getOpacityInternal() + fadeFactor, aimed));
            else if (blurVisual.getOpacityInternal() > aimed)
                blurVisual.setOpacityInternal((float) Math.max(blurVisual.getOpacityInternal() - fadeFactor, aimed));
            
        } else
            blurVisual.setOpacityInternal(0);
    }
    
}

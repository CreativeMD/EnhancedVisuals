package team.creative.enhancedvisuals.common.addon.toughasnails;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;
import toughasnails.api.temperature.TemperatureHelper;
import toughasnails.api.temperature.TemperatureLevel;

public class TemperatureHandler extends VisualHandler {
    
    @CreativeConfig
    public double fadeFactor = 0.005;
    
    @CreativeConfig
    public VisualType freeze = new VisualTypeOverlay("freeze");
    public Visual freezeVisual;
    
    @CreativeConfig
    public VisualType heat = new VisualTypeOverlay("heat");
    public Visual heatVisual;
    
    @CreativeConfig
    public double icyIntensity = 0.3;
    @CreativeConfig
    public double coldIntensity = 0.2;
    @CreativeConfig
    public double neutralIntensity = 0;
    @CreativeConfig
    public double warmIntensity = 0.2;
    @CreativeConfig
    public double hotIntensity = 0.3;
    
    @Override
    public void tick(@Nullable Player player) {
        if (freezeVisual == null) {
            freezeVisual = new Visual(freeze, this, 0);
            freezeVisual.setOpacityInternal(0);
            VisualManager.add(freezeVisual);
            
            heatVisual = new Visual(heat, this, 0);
            heatVisual.setOpacityInternal(0);
            VisualManager.add(heatVisual);
        }
        
        double aimedHeat = neutralIntensity;
        double aimedFreeze = neutralIntensity;
        TemperatureLevel temp = TemperatureLevel.NEUTRAL;
        if (player != null && player.isAlive() && TemperatureHelper.isTemperatureEnabled())
            temp = TemperatureHelper.getTemperatureForPlayer(player);
        
        switch (temp) {
            case ICY:
                aimedHeat = neutralIntensity;
                aimedFreeze = icyIntensity;
                break;
            case COLD:
                aimedHeat = neutralIntensity;
                aimedFreeze = coldIntensity;
                break;
            case NEUTRAL:
                aimedHeat = neutralIntensity;
                aimedFreeze = neutralIntensity;
                break;
            case WARM:
                aimedHeat = warmIntensity;
                aimedFreeze = neutralIntensity;
                break;
            case HOT:
                aimedHeat = hotIntensity;
                aimedFreeze = neutralIntensity;
                break;
        }
        
        if (freezeVisual.getOpacityInternal() < aimedFreeze)
            freezeVisual.setOpacityInternal((float) Math.min(freezeVisual.getOpacityInternal() + fadeFactor, aimedFreeze));
        else if (freezeVisual.getOpacityInternal() > aimedFreeze)
            freezeVisual.setOpacityInternal((float) Math.max(freezeVisual.getOpacityInternal() - fadeFactor, aimedFreeze));
        
        if (heatVisual.getOpacityInternal() < aimedHeat)
            heatVisual.setOpacityInternal((float) Math.min(heatVisual.getOpacityInternal() + fadeFactor, aimedHeat));
        else if (heatVisual.getOpacityInternal() > aimedHeat)
            heatVisual.setOpacityInternal((float) Math.max(heatVisual.getOpacityInternal() - fadeFactor, aimedHeat));
    }
    
}

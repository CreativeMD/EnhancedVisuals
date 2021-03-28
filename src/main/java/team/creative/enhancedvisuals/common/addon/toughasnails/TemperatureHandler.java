package team.creative.enhancedvisuals.common.addon.toughasnails;

import javax.annotation.Nullable;

import com.creativemd.creativecore.common.config.api.CreativeConfig;

import net.minecraft.entity.player.EntityPlayer;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;
import toughasnails.api.TANCapabilities;
import toughasnails.api.stat.capability.ITemperature;
import toughasnails.api.temperature.Temperature;
import toughasnails.api.temperature.TemperatureScale.TemperatureRange;

public class TemperatureHandler extends VisualHandler {
    
    @CreativeConfig
    public double defaultIntensity = 0;
    @CreativeConfig
    public double mediumIntensity = 0.2;
    @CreativeConfig
    public double maxIntensity = 0.4;
    @CreativeConfig
    public double fadeFactor = 0.005;
    
    @CreativeConfig
    public VisualType freeze = new VisualTypeOverlay("freeze");
    public Visual freezeVisual;
    
    @CreativeConfig
    public VisualType heat = new VisualTypeOverlay("heat");
    public Visual heatVisual;
    
    private static Temperature defaultTemperature = new Temperature(12);
    
    @Override
    public void tick(@Nullable EntityPlayer player) {
        if (freezeVisual == null) {
            freezeVisual = new Visual(freeze, this, 0);
            freezeVisual.setOpacityInternal(0);
            VisualManager.add(freezeVisual);
            
            heatVisual = new Visual(heat, this, 0);
            heatVisual.setOpacityInternal(0);
            VisualManager.add(heatVisual);
        }
        
        double aimedHeat = defaultIntensity;
        double aimedFreeze = defaultIntensity;
        Temperature temp = null;
        
        if (player != null)
            temp = ((ITemperature) player.getCapability(TANCapabilities.TEMPERATURE, null)).getTemperature();
        
        if (temp == null)
            temp = defaultTemperature;
        
        TemperatureRange range = temp.getRange();
        switch (range) {
        case ICY:
            aimedHeat = 0;
            aimedFreeze = maxIntensity;
            break;
        case COOL:
            aimedHeat = 0;
            aimedFreeze = mediumIntensity * temp.getRangeDelta(true);
            
            break;
        case MILD:
            aimedHeat = defaultIntensity;
            aimedFreeze = defaultIntensity;
            break;
        case WARM:
            aimedHeat = mediumIntensity * temp.getRangeDelta(false);
            aimedFreeze = 0;
            break;
        case HOT:
            aimedHeat = maxIntensity;
            aimedFreeze = 0;
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

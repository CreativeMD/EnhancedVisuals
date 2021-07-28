package team.creative.enhancedvisuals.common.addon.survive;

import javax.annotation.Nullable;

import net.minecraft.world.entity.player.Player;
import team.creative.creativecore.common.config.api.CreativeConfig;
import team.creative.enhancedvisuals.api.Visual;
import team.creative.enhancedvisuals.api.VisualHandler;
import team.creative.enhancedvisuals.api.type.VisualType;
import team.creative.enhancedvisuals.api.type.VisualTypeOverlay;
import team.creative.enhancedvisuals.client.VisualManager;

public class TemperatureHandler extends VisualHandler {
    
    @CreativeConfig
    public double defaultIntensity = 0;
    @CreativeConfig
    public double mediumIntensity = 0.2;
    @CreativeConfig
    public double maxIntensity = 0.3;
    @CreativeConfig
    public double fadeFactor = 0.005;
    
    @CreativeConfig
    public VisualType freeze = new VisualTypeOverlay("freeze");
    public Visual freezeVisual;
    
    @CreativeConfig
    public VisualType heat = new VisualTypeOverlay("heat");
    public Visual heatVisual;
    
    @CreativeConfig
    public double coldestTemperature = 33;
    @CreativeConfig
    public double hypotheremiaTemperature = 35;
    @CreativeConfig
    public double coldTemperature = 36;
    
    @CreativeConfig
    public double defaultTemperature = 37;
    
    @CreativeConfig
    public double warmTemperature = 38;
    @CreativeConfig
    public double hyperthermiaTemperature = 39;
    @CreativeConfig
    public double hottestTemperature = 41;
    
    public double getTemperature(Player player) {
        return player.getPersistentData().getCompound("survive:PlayerData").getCompound("TemperatureStats").getDouble("temperatureLevel");
    }
    
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
        
        double aimedHeat = defaultIntensity;
        double aimedFreeze = defaultIntensity;
        double temp = defaultTemperature;
        if (player != null && player.isAlive())
            temp = getTemperature(player);
        
        if (temp <= coldestTemperature) {
            aimedHeat = 0;
            aimedFreeze = maxIntensity;
        } else if (temp < hypotheremiaTemperature) {
            aimedHeat = 0;
            aimedFreeze = (1 - (temp - coldestTemperature) / (hypotheremiaTemperature - coldestTemperature)) * (maxIntensity - mediumIntensity) + mediumIntensity;
        } else if (temp < coldTemperature) {
            aimedHeat = 0;
            aimedFreeze = (1 - (temp - hypotheremiaTemperature) / (coldTemperature - hypotheremiaTemperature)) * mediumIntensity;
        } else if (temp >= hottestTemperature) {
            aimedHeat = maxIntensity;
            aimedFreeze = 0;
        } else if (temp > hyperthermiaTemperature) {
            aimedHeat = (hottestTemperature - temp) / (hottestTemperature - hyperthermiaTemperature) * (maxIntensity - mediumIntensity) + mediumIntensity;
            aimedFreeze = 0;
        } else if (temp > warmTemperature) {
            aimedHeat = (hyperthermiaTemperature - temp) / (hyperthermiaTemperature - warmTemperature) * mediumIntensity;
            aimedFreeze = 0;
        } else {
            aimedHeat = 0;
            aimedFreeze = 0;
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

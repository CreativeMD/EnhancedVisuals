package team.creative.enhancedvisuals.common.addon.survive;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
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
	
	public double getTemperature(PlayerEntity player) {
		return player.getPersistentData().getDouble("survive:PlayerData#TemperatureStats#temperatureLevel");
	}
	
	@Override
	public void tick(@Nullable PlayerEntity player) {
		if (freezeVisual == null) {
			freezeVisual = new Visual(freeze, 0);
			freezeVisual.opacity = 0;
			VisualManager.add(freezeVisual);
			
			heatVisual = new Visual(heat, 0);
			heatVisual.opacity = 0;
			VisualManager.add(heatVisual);
		}
		
		double aimedHeat = defaultIntensity;
		double aimedFreeze = defaultIntensity;
		double temp = defaultTemperature;
		if (player != null)
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
		
		if (freezeVisual.opacity < aimedFreeze)
			freezeVisual.opacity = (float) Math.min(freezeVisual.opacity + fadeFactor, aimedFreeze);
		else if (freezeVisual.opacity > aimedFreeze)
			freezeVisual.opacity = (float) Math.max(freezeVisual.opacity - fadeFactor, aimedFreeze);
		
		if (heatVisual.opacity < aimedHeat)
			heatVisual.opacity = (float) Math.min(heatVisual.opacity + fadeFactor, aimedHeat);
		else if (heatVisual.opacity > aimedHeat)
			heatVisual.opacity = (float) Math.max(heatVisual.opacity - fadeFactor, aimedHeat);
	}
	
}
